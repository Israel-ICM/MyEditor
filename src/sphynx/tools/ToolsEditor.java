package sphynx.tools;

/**
 *
 * @author Israel-ICM
 */
public class ToolsEditor {
    /**
     * Verifica si el cursor 
     * @param text
     * @param indexCursorPosition
     * @return 
     */
    public boolean esComentario(String text, int indexCursorPosition) {
        int indexHaciaAtras = indexCursorPosition;
        int indexHaciaAdelante = indexCursorPosition;
        while (indexHaciaAtras >= 0) {
            if (text.charAt(indexHaciaAtras) == '/' && text.charAt(indexHaciaAtras - 1) == '*') // Si encontramos */
                return false;
            if (text.charAt(indexHaciaAtras) == '*' && text.charAt(indexHaciaAtras - 1) == '*') // Si encontramos /*
                return true;
            indexHaciaAtras--;
        }
        return false;
    }
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
            while (!finInicioFila && indexCursorPosition > -1) {
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
