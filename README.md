LunchFlow - Sistema de Autogestión Comedor Corporativo.

Este proyecto es una aplicación nativa para Android desarrollada en el marco de la Tecnicatura en Programación (UTN FRGP). 

El objetivo es digitalizar la gestión de viandas en entornos corporativos, permitiendo a los usuarios organizar su semana gastronómica de manera eficiente.

📋 Funcionalidades del Sistema:

Autenticación: Módulo de Login con validación de estados y opción de persistencia de sesión.

Navegación Semanal: Selector de días mediante MaterialButtonToggleGroup con estilos personalizados para evitar la estética default del SDK.

Catálogo de Menús: Implementación de ViewPager2 y RecyclerView para la visualización dinámica de platos.

Módulo de Cuenta: Gestión de perfil, historial de pedidos y ajustes del sistema.

Resumen de Selección: Pantalla de confirmación que consolida los platos elegidos antes de la persistencia final.

🛠 Detalles de Implementación (Análisis Técnico)
Arquitectura de UI: Se utilizó un enfoque responsivo basado en ConstraintLayout, resolviendo problemas de adaptabilidad en dispositivos de gran escala (como el Samsung S21 FE) mediante el uso de anclajes relativos y pesos (weight) en lugar de dimensiones fijas.

Consistencia Visual: Aplicación de la fuente Montserrat mediante la configuración del archivo de temas (Themes.xml) para garantizar uniformidad en toda la experiencia de usuario.

Gestión de Recursos XML: Uso de selectores (selector.xml) para controlar estados de interacción en CheckBoxes y botones, y drawables con canales alpha para lograr la estética neo-industrial traslúcida.

Ciclo de Vida y Navegación: Optimización de la pila de actividades (Backstack) mediante el uso de finish() en actividades secundarias, evitando la recreación innecesaria de la MainActivity y preservando el estado de la selección en memoria.
