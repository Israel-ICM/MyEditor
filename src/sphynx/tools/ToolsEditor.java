package sphynx.tools;

/**
 *
 * @author Israel-ICM
 */
public class ToolsEditor {
    public static boolean existComment(String text) {
        if (text.contains("/*") && !text.contains("\"/*\""))
            return true;
        else if (text.contains("//") && !text.contains("\"//\""))
            return true;
        else
            return false;
    }
    public static int getIndexInicioPalabra(String texto, int indexDesde) {
        while (texto.length() > 0 && indexDesde >= 0) {
            // Si hay un espacio, tabulador o salto de linea entonces se toma como fin de la palabra
            if (String.valueOf(texto.charAt(indexDesde)).matches(".*[ \n\t].*")) {
                return indexDesde; // Se suma 1 por el espacio, tabulador o salto
            }
            indexDesde--;
        }
        return 0;
    }
    public static int getIndexFinPalabra(String texto, int indexDesde) {
        int indexFinalPalabra = 0;
        while (indexDesde < texto.length()) {
            // Si hay un espacio, tabulador o salto de linea entonces se toma como fin de la palabra
            if (String.valueOf(texto.charAt(indexDesde)).matches(".*[ \n\t].*")) {
                indexFinalPalabra = indexDesde;
                break;
            }
            else
                indexFinalPalabra = texto.length();
            indexDesde++;
        }
        return indexFinalPalabra;
    }
    /**
     * Verifica si el cursor 
     * @param text
     * @param indexCursorPosition
     * @return 
     */
    public static boolean estaEnComentario(String text, int indexCursorPosition) {
        int indexAux = indexCursorPosition;
        while (text.length() > 1 && indexAux > 0) {
            String tagComentario = String.valueOf(text.charAt(indexAux - 1)) + String.valueOf(text.charAt(indexAux));
            if (tagComentario.contains("*/"))
                return false;
            else if (tagComentario.contains("/*")) {
                return true;
            }
            indexAux--;
        }
        return false;
    }
    public static int getIndexInicioComment(String text, int index) {
        if (text.length() > 1) {
            try {
                while (index > -1) {
                    if ((text.charAt(index) == '*' && text.charAt(index - 1) == '/') || (text.charAt(index) == '/' && text.charAt(index + 1) == '*')) {
                        return index - 1;
                    }
                    index--;
                }
            }
            catch (Exception e) {}
        }
        return -1;
    }
    public static int getIndexFinComment(String text, int index) {
        if (text.length() > 1) {
            try {
                while (index < text.length()) {
                    if ((text.charAt(index) == '*' && text.charAt(index + 1) == '/') || (text.charAt(index) == '/' && text.charAt(index - 1) == '*')) {
                        return index + 1;
                    }
                    index++;
                }
            }
            catch (Exception e) {}
        }
        return text.length();
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
