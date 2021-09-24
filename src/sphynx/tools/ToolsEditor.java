package sphynx.tools;

/**
 *
 * @author Israel-ICM
 */
public class ToolsEditor {
    /**
     * Retorna la cantidad de tabuladores que existe en la linea definida
     * @param text
     * @param indexCursorPosition
     * @return 
     */
    public static int getCantidadTabsLinea(String text, int indexCursorPosition) {
        boolean finInicioFila = false;
        int tabs = 0;
        if (getCantidadLineas(text) > 1) {
            while (!finInicioFila) {
                if (text.charAt(indexCursorPosition) == '\n')
                    finInicioFila = true;
                if (text.charAt(indexCursorPosition) == '\t')
                    tabs++;
                indexCursorPosition--;
            }
        }
        return tabs;
    }
    /**
     * Retorna la cantidad de lineas de un texto
     * @param text
     * @return 
     */
    public static int getCantidadLineas(String text) {
        return text.split("\n").length;
    }
    /**
     * Repite una cadena el número de veces necesario
     * @param textRepeat
     * @param veces
     * @return 
     */
    public static String repeatText(String textRepeat, int veces) {
        String text = "";
        for (int i = 0; i < veces; i++) {
            text += textRepeat;
        }
        return text;
    }
}
