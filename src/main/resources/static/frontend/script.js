
// La página se sirve desde Spring Boot, por eso usamos rutas relativas.
const $ = id => document.getElementById(id);

// Petición a la API
async function peticion(ruta, metodo = "GET", datos = null) {
    const opciones = { method: metodo };

    if (datos !== null) {
        opciones.headers = { "Content-Type": "application/json" };
        opciones.body = JSON.stringify(datos);
    }

    const respuesta = await fetch(ruta, opciones);

    if (!respuesta.ok) {
        throw new Error("Error HTTP " + respuesta.status);
    }

    if (respuesta.status === 204) {
        return null;
    }

    return respuesta.json();
}

function mensaje(id, texto) {
    $(id).textContent = texto;
}

// Crea una fila con botones para editar y eliminar
function ponerFila(lista, descripcion, editar, borrar) {
    const fila = document.createElement("div");
    fila.className = "resultado";

    const texto = document.createElement("span");
    texto.textContent = descripcion;

    const acciones = document.createElement("div");
    acciones.className = "acciones";

    const botonEditar = document.createElement("button");
    botonEditar.textContent = "Editar";
    botonEditar.onclick = editar;

    const botonBorrar = document.createElement("button");
    botonBorrar.textContent = "Eliminar";
    botonBorrar.className = "peligro";
    botonBorrar.onclick = borrar;

    acciones.append(botonEditar, botonBorrar);
    fila.append(texto, acciones);
    lista.appendChild(fila);
}

async function eliminar(ruta, cargarListado) {
    if (!confirm("¿Seguro que quieres eliminar este registro?")) {
        return;
    }

    try {
        await peticion(ruta, "DELETE");
        await cargarListado();
        await cargarOpciones();
    } catch (error) {
        alert("No se pudo eliminar. Puede que el registro esté utilizado en una reserva. " + error.message);
    }
}

// ---------- AULAS ----------

async function cargarAulas(ruta = "/aulas") {
    const lista = $("listaAulas");
    lista.textContent = "Cargando...";

    try {
        const aulas = await peticion(ruta);
        lista.innerHTML = "";

        if (aulas.length === 0) {
            lista.textContent = "No se encontraron aulas.";
        }

        aulas.forEach(aula => ponerFila(
            lista,
            `#${aula.id} · ${aula.nombre} · Capacidad: ${aula.capacidad} · Ordenadores: ${aula.ordenadores ? "Sí" : "No"}`,
            () => {
                $("aulaId").value = aula.id;
                $("aulaNombre").value = aula.nombre;
                $("aulaCapacidad").value = aula.capacidad;
                $("aulaOrdenadores").value = String(aula.ordenadores);
                $("formAula").scrollIntoView({ behavior: "smooth" });
            },
            () => eliminar("/aulas/" + aula.id, cargarAulas)
        ));

    } catch (error) {
        lista.textContent = "No se pudieron cargar las aulas: " + error.message;
    }
}

$("formAula").onsubmit = async evento => {
    evento.preventDefault();

    const id = $("aulaId").value;

    const datos = {
        nombre: $("aulaNombre").value.trim(),
        capacidad: Number($("aulaCapacidad").value),
        ordenadores: $("aulaOrdenadores").value === "true"
    };

    if (!datos.nombre || datos.capacidad < 1) {
        mensaje("mensajeAula", "Introduce datos válidos.");
        return;
    }

    try {
        await peticion(
            id ? "/aulas/" + id : "/aulas",
            id ? "PUT" : "POST",
            datos
        );

        $("formAula").reset();
        $("aulaId").value = "";

        mensaje("mensajeAula", "Aula guardada correctamente.");
        await cargarAulas();
        await cargarOpciones();

    } catch (error) {
        mensaje("mensajeAula", error.message);
    }
};

$("limpiarAula").onclick = () => {
    $("formAula").reset();
    $("aulaId").value = "";
};

$("todasAulas").onclick = () => cargarAulas();

$("buscarAulas").onclick = () => {
    const ordenadores = $("filtroOrdenadores").value;
    const capacidad = $("filtroCapacidad").value;

    // El backend actual admite un filtro cada vez
    let ruta = "/aulas/buscar";

    if (ordenadores !== "") {
        ruta += "?ordenadores=" + ordenadores;
    } else if (capacidad !== "") {
        ruta += "?capacidad=" + capacidad;
    }

    cargarAulas(ruta);
};

// ---------- PROFESORES ----------

async function cargarProfesores() {
    const lista = $("listaProfesores");

    try {
        const profesores = await peticion("/profesores");
        lista.innerHTML = "";

        if (!profesores.length) {
            lista.textContent = "No hay profesores registrados.";
        }

        profesores.forEach(profesor => ponerFila(
            lista,
            `#${profesor.id} · ${profesor.nombre} ${profesor.apellidos} · ${profesor.email}`,
            () => {
                $("profesorId").value = profesor.id;
                $("profesorNombre").value = profesor.nombre;
                $("profesorApellidos").value = profesor.apellidos;
                $("profesorEmail").value = profesor.email;
                $("formProfesor").scrollIntoView({ behavior: "smooth" });
            },
            () => eliminar("/profesores/" + profesor.id, cargarProfesores)
        ));

    } catch (error) {
        lista.textContent = "Error al cargar profesores: " + error.message;
    }
}

$("formProfesor").onsubmit = async evento => {
    evento.preventDefault();

    const id = $("profesorId").value;

    const datos = {
        nombre: $("profesorNombre").value.trim(),
        apellidos: $("profesorApellidos").value.trim(),
        email: $("profesorEmail").value.trim()
    };

    try {
        await peticion(
            id ? "/profesores/" + id : "/profesores",
            id ? "PUT" : "POST",
            datos
        );

        $("formProfesor").reset();
        $("profesorId").value = "";

        mensaje("mensajeProfesor", "Profesor guardado correctamente.");
        await cargarProfesores();
        await cargarOpciones();

    } catch (error) {
        mensaje("mensajeProfesor", error.message);
    }
};

$("limpiarProfesor").onclick = () => {
    $("formProfesor").reset();
    $("profesorId").value = "";
};

$("recargarProfesores").onclick = cargarProfesores;

// ---------- FRANJAS HORARIAS ----------

async function cargarFranjas() {
    const lista = $("listaFranjas");

    try {
        const franjas = await peticion("/franjas");
        lista.innerHTML = "";

        if (!franjas.length) {
            lista.textContent = "No hay franjas registradas.";
        }

        franjas.forEach(franja => ponerFila(
            lista,
            `#${franja.id} · ${franja.horaInicio} - ${franja.horaFin}`,
            () => {
                $("franjaId").value = franja.id;
                $("franjaInicio").value = franja.horaInicio.slice(0, 5);
                $("franjaFin").value = franja.horaFin.slice(0, 5);
                $("formFranja").scrollIntoView({ behavior: "smooth" });
            },
            () => eliminar("/franjas/" + franja.id, cargarFranjas)
        ));

    } catch (error) {
        lista.textContent = "Error al cargar franjas: " + error.message;
    }
}

$("formFranja").onsubmit = async evento => {
    evento.preventDefault();

    const id = $("franjaId").value;

    const datos = {
        horaInicio: $("franjaInicio").value,
        horaFin: $("franjaFin").value
    };

    if (datos.horaInicio >= datos.horaFin) {
        mensaje("mensajeFranja", "La hora de fin debe ser posterior al inicio.");
        return;
    }

    try {
        await peticion(
            id ? "/franjas/" + id : "/franjas",
            id ? "PUT" : "POST",
            datos
        );

        $("formFranja").reset();
        $("franjaId").value = "";

        mensaje("mensajeFranja", "Franja guardada correctamente.");
        await cargarFranjas();
        await cargarOpciones();

    } catch (error) {
        mensaje("mensajeFranja", error.message);
    }
};

$("limpiarFranja").onclick = () => {
    $("formFranja").reset();
    $("franjaId").value = "";
};

$("recargarFranjas").onclick = cargarFranjas;

// ---------- OPCIONES PARA RESERVAR ----------

function rellenarSelect(id, registros, descripcion) {
    const select = $(id);
    const valorAnterior = select.value;

    select.innerHTML = "";

    registros.forEach(registro => {
        const opcion = document.createElement("option");
        opcion.value = registro.id;
        opcion.textContent = descripcion(registro);
        select.appendChild(opcion);
    });

    if (valorAnterior) {
        const existe = registros.some(registro => String(registro.id) === valorAnterior);
        if (existe) {
            select.value = valorAnterior;
        }
    }
}

async function cargarOpciones() {
    try {
        const aulas = await peticion("/aulas");
        const profesores = await peticion("/profesores");
        const franjas = await peticion("/franjas");

        rellenarSelect("reservaAula", aulas, a => `${a.id} - ${a.nombre}`);
        rellenarSelect("reservaProfesor", profesores, p => `${p.id} - ${p.nombre} ${p.apellidos}`);
        rellenarSelect("reservaFranja", franjas, f => `${f.id} - ${f.horaInicio} a ${f.horaFin}`);

    } catch (error) {
        mensaje("mensajeReserva", "No se pudieron cargar las opciones de reserva.");
    }
}

// ---------- RESERVAS ----------

async function cargarReservas() {
    const lista = $("listaReservas");

    try {
        const reservas = await peticion("/reservas");
        lista.innerHTML = "";

        if (!reservas.length) {
            lista.textContent = "No hay reservas registradas.";
        }

        reservas.forEach(reserva => ponerFila(
            lista,
            `#${reserva.id} · ${reserva.fecha} · Aula ${reserva.aula?.id ?? "?"} · Profesor ${reserva.profesor?.id ?? "?"} · Franja ${reserva.franjaHoraria?.id ?? "?"}`,
            async () => {
                await cargarOpciones();

                $("reservaId").value = reserva.id;
                $("reservaFecha").value = reserva.fecha;
                $("reservaAula").value = reserva.aula.id;
                $("reservaProfesor").value = reserva.profesor.id;
                $("reservaFranja").value = reserva.franjaHoraria.id;

                $("formReserva").scrollIntoView({ behavior: "smooth" });
            },
            () => eliminar("/reservas/" + reserva.id, cargarReservas)
        ));

    } catch (error) {
        lista.textContent = "Error al cargar reservas: " + error.message;
    }
}

$("formReserva").onsubmit = async evento => {
    evento.preventDefault();

    const id = $("reservaId").value;
    const aulaId = Number($("reservaAula").value);
    const profesorId = Number($("reservaProfesor").value);
    const franjaId = Number($("reservaFranja").value);

    if (!aulaId || !profesorId || !franjaId) {
        mensaje("mensajeReserva", "Primero registra un aula, un profesor y una franja.");
        return;
    }

    const datos = {
        fecha: $("reservaFecha").value,
        aula: { id: aulaId },
        profesor: { id: profesorId },
        franjaHoraria: { id: franjaId }
    };

    try {
        await peticion(
            id ? "/reservas/" + id : "/reservas",
            id ? "PUT" : "POST",
            datos
        );

        $("formReserva").reset();
        $("reservaId").value = "";

        mensaje("mensajeReserva", "Reserva guardada correctamente.");
        await cargarReservas();

    } catch (error) {
        mensaje(
            "mensajeReserva",
            "No se pudo guardar la reserva (" + error.message +
            "). Comprueba la fecha y que el aula no esté ocupada."
        );
    }
};

$("limpiarReserva").onclick = () => {
    $("formReserva").reset();
    $("reservaId").value = "";
};

$("recargarReservas").onclick = cargarReservas;

// Cargar los listados al abrir la página
cargarAulas();
cargarProfesores();
cargarFranjas();
cargarOpciones();
cargarReservas();
