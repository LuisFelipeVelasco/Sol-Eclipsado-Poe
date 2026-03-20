# Sol Eclipsado 🌞🌑

**Sol Eclipsado** es un juego interactivo desarrollado en **Java con JavaFX**. El objetivo principal es adivinar una palabra oculta; sin embargo, por cada intento incorrecto, un sol en la pantalla se irá eclipsando progresivamente a través de una secuencia de imágenes. ¡Adivina la palabra antes de que el eclipse sea total!

Enlace al repositorio: [Sol-Eclipsado-Poe](https://github.com/LuisFelipeVelasco/Sol-Eclipsado-Poe)

---

## 🚀 Características Principales

* **Interfaz Gráfica Dinámica:** Desarrollada con JavaFX, el juego actualiza en tiempo real la imagen del sol (`Sol_1.png` a `Sol_5.png`) según los errores del jugador.
* **Procesamiento de Texto Avanzado:** El juego es capaz de recibir palabras con tildes, la letra 'ñ' o mayúsculas, y estandarizarlas internamente para una comparación justa y exacta.
* **Validaciones Estrictas:** Se asegura de que el usuario ingrese únicamente palabras válidas usando Expresiones Regulares (Regex).

---

## 🛠️ Tecnologías y Conceptos Aplicados

A lo largo del desarrollo de este proyecto, se implementaron soluciones técnicas clave:

1. **Expresiones Regulares (Regex):** Se utiliza `matches("\\p{L}+")` para garantizar que la entrada del usuario consista en **una o más letras** de cualquier idioma, rechazando números y espacios en blanco.
2. **Normalización de Strings:** Para evaluar correctamente palabras acentuadas (ej. "Árbol" vs "arbol"), se implementó `Normalizer.Form.NFD` en combinación con `replaceAll("\\p{M}", "")`. Esto separa las vocales de sus tildes o diéresis y las elimina, convirtiendo finalmente el texto a minúsculas universales con `toLowerCase(Locale.ROOT)`.
3. **Carga de Recursos en JavaFX:** Las imágenes del eclipse se cargan dinámicamente convirtiendo la ruta interna del proyecto a un formato externo legible por JavaFX mediante `getClass().getResource(...).toExternalForm()`.
4. **Control de Versiones Limpio:** El proyecto cuenta con un `.gitignore` configurado específicamente para excluir carpetas autogeneradas como `.idea/` y `target/`, manteniendo el repositorio ligero.

---

## ⚙️ Requisitos Previos

Para ejecutar y modificar este proyecto en tu entorno local, necesitas:

* **Java Development Kit (JDK):** Versión 17 o superior.
* **JavaFX:** Configurado en tu entorno o gestionado a través de las dependencias.
* **IDE Recomendado:** IntelliJ IDEA, Eclipse o NetBeans.

---

## 📦 Recomendación Importante: Uso de Maven (`pom.xml`)

Este proyecto está construido utilizando **Maven** como gestor de dependencias y empaquetador. 

**Si descargas o clonas este proyecto por primera vez en IntelliJ IDEA:**
1. No te preocupes si no ves el botón de "Play" (Run) de inmediato.
2. Abre el panel de tu proyecto, busca el archivo **`pom.xml`**.
3. Haz clic derecho sobre él y selecciona **"Add as Maven Project"** (o "Reload project" desde la pestaña de Maven).
4. Maven descargará automáticamente lo necesario y configurará la estructura del proyecto. 

*(Nota: La carpeta `target/` generada por Maven es el directorio de compilación y es ignorada en este repositorio por buenas prácticas).*

---

## 🏃‍♂️ Cómo Ejecutar el Proyecto Localmente

1. Clona este repositorio en tu máquina:
   ```bash
   git clone [https://github.com/LuisFelipeVelasco/Sol-Eclipsado-Poe.git](https://github.com/LuisFelipeVelasco/Sol-Eclipsado-Poe.git)
