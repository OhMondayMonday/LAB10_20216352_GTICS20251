function explotarCoordenada() {
    const input = document.getElementById('coordenada');
    const coordenada = input.value.trim();

    if (!coordenada) {
        alert('Por favor, ingrese una coordenada válida (Ej: 1 5)');
        return;
    }

    const partes = coordenada.split(/\s+/);
    if (partes.length !== 2) {
        alert('Formato incorrecto. Use el formato: fila columna (Ej: 1 5)');
        return;
    }

    const x = parseInt(partes[0]) - 1; // Convertir a índice base 0
    const y = parseInt(partes[1]) - 1; // Convertir a índice base 0

    if (isNaN(x) || isNaN(y)) {
        alert('Las coordenadas deben ser números válidos');
        return;
    }

    // Verificar que las coordenadas estén dentro del rango
    const maxX = document.querySelectorAll('.fila').length;
    const maxY = document.querySelectorAll('.fila')[0].children.length;

    if (x < 0 || x >= maxX || y < 0 || y >= maxY) {
        alert(`Las coordenadas deben estar entre 1-${maxX} y 1-${maxY}`);
        return;
    }

    // Verificar si la celda ya está revelada
    const celda = document.querySelector('.celda[data-x="' + x + '"][data-y="' + y + '"]');
    if (celda.classList.contains('revelada')) {
        alert('Esta casilla ya ha sido explorada');
        return;
    }

    // Limpiar el input
    input.value = '';

    // Realizar la petición AJAX
    fetch('/buscaminas/click', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'x=' + x + '&y=' + y
    })
        .then(response => response.json())
        .then(data => {
            actualizarTablero(data);
            actualizarInfo(data);

            if (data.juegoTerminado) {
                document.getElementById('coordenada').disabled = true;
                document.querySelector('.btn-explotar').disabled = true;

                if (data.ganado) {
                    crearEfectoVictoria();
                } else {
                    crearEfectoExplosion();
                }
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al procesar la coordenada. Intente nuevamente.');
        });
}

// Permitir usar Enter en el campo de coordenadas
document.addEventListener('DOMContentLoaded', function () {
    const input = document.getElementById('coordenada');
    if (input) {
        input.addEventListener('keypress', function (e) {
            if (e.key === 'Enter') {
                explotarCoordenada();
            }
        });

        // Verificar si el juego ya terminó
        const mensajeElement = document.querySelector('.mensaje');
        if (mensajeElement && (mensajeElement.classList.contains('victoria') || mensajeElement.classList.contains('bomba'))) {
            input.disabled = true;
            document.querySelector('.btn-explotar').disabled = true;
        }
    }
});

function actualizarTablero(juego) {
    for (let x = 0; x < juego.dimX; x++) {
        for (let y = 0; y < juego.dimY; y++) {
            const celda = document.querySelector('.celda[data-x="' + x + '"][data-y="' + y + '"]');
            const celdaData = juego.tablero[x][y];

            if (celdaData.revelada) {
                celda.classList.add('revelada');

                if (celdaData.bomba) {
                    celda.classList.add('bomba');
                    if (celdaData.exploto) {
                        celda.classList.add('exploto');
                    }
                } else if (celdaData.numeroVecinas > 0) {
                    celda.classList.add('numero-' + celdaData.numeroVecinas);
                    celda.innerHTML = '<span>' + celdaData.numeroVecinas + '</span>';
                }
            }
        }
    }
}

function actualizarInfo(juego) {
    document.querySelector('.intentos span').textContent = juego.intentosRestantes;
    const mensajeElement = document.querySelector('.mensaje');
    mensajeElement.textContent = juego.mensaje;

    mensajeElement.className = 'mensaje';
    if (juego.juegoTerminado) {
        if (juego.ganado) {
            mensajeElement.classList.add('victoria');
        } else {
            mensajeElement.classList.add('bomba');
        }
    }
}

function crearEfectoVictoria() {
    for (let i = 0; i < 20; i++) {
        setTimeout(() => {
            const particula = document.createElement('div');
            particula.className = 'particula';
            particula.style.left = Math.random() * 100 + '%';
            particula.style.animationDelay = Math.random() * 2 + 's';
            particula.style.background = '#' + Math.floor(Math.random() * 16777215).toString(16);
            document.getElementById('particulas').appendChild(particula);

            setTimeout(() => {
                particula.remove();
            }, 6000);
        }, i * 100);
    }
}

function crearEfectoExplosion() {
    document.body.style.animation = 'shake 0.5s ease-in-out 3';
    setTimeout(() => {
        document.body.style.animation = '';
    }, 1500);
}

// Efecto de partículas de fondo
setInterval(() => {
    if (document.querySelectorAll('.particula').length < 5) {
        const particula = document.createElement('div');
        particula.className = 'particula';
        particula.style.left = Math.random() * 100 + '%';
        particula.style.animationDelay = '0s';
        document.getElementById('particulas').appendChild(particula);

        setTimeout(() => {
            particula.remove();
        }, 6000);
    }
}, 2000);