package sphynx.styleDocuments;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import sphynx.tools.ToolsEditor;

/**
 * @author israel-icm
 */
public class DefaultStyleDocumentPHP extends DefaultStyledDocument{
    // final String palabras = "";
    final String methodClass = "public|private|class|extends|implements|static|abstract|interface|function";
    final String imports = "require|require_once|include|include_once";
    final String condicionales = "if|else|elseif|foreach|for|while|return|true|false|null|new|throw|as|echo";
    final String tipoDatos = "int|float|bool|object|stdClass|string|void|var|const";
    final StyleContext styleContext = StyleContext.getDefaultStyleContext();
    // Instanciamos los atributos para los estilos para las palabras normales y las que tienes listadas
    // En este caso Azul para las palabras especiales y negro para las normales
    final AttributeSet attribMethodClass = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#4B0CAA"));
    final AttributeSet attribImports = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#395BBD"));
    final AttributeSet attribCondicionales = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode(/*"#570FEA"*/"#395BBD"));
    final AttributeSet attribTipoDatos = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#B148CE"));
    final AttributeSet attribComentarios = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#908E8E"));
    final AttributeSet attribString = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#048114"));
    final AttributeSet attribCadenas = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#1E9E51"));
    final AttributeSet attribVariables = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#7B0407"));
    final AttributeSet attribNormal = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.BLACK);

    private int[] getIndexComment(String text, int index) {
        int inicio = -1;
        int fin = -1;
        if (text.length() > 1) {
            try {
                // hacia atrás
                while (index > -1) {
                    if ((text.charAt(index) == '*' && text.charAt(index - 1) == '/') || (text.charAt(index) == '/' && text.charAt(index + 1) == '*')) {
                        inicio = index - 1;
                    }
                    index--;
                }
            }
            catch (Exception e) {}
        }
        return new int[]{inicio, fin};
    }

    /**
     * Estiliza una cierta porción de código
     * @param indexInicio
     * @param indexFin
     * @throws BadLocationException 
     */
    private void estilizarPorcionCodigo(int indexInicio, int indexFin) throws BadLocationException {
        String textoCompleto = getText(0, getLength());
        String texto = textoCompleto.substring(indexInicio, indexFin);
        /*int indexInicioPalabra = ToolsEditor.getIndexInicioPalabra(texto, indexCursor);
        int indexFinalPalabra = ToolsEditor.getIndexFinPalabra(texto, indexCursor);
        setStylePalabra(indexInicioPalabra, indexFinalPalabra);*/
    }

    /**
     * Estiliza el código a partir del índice del cursor
     * @param indexInicio
     * @param indexFin
     * @param indexCursor
     * @throws BadLocationException 
     */
    private void estilizarCodigo2(int indexCursor) throws BadLocationException {
        indexCursor = indexCursor < 0 ? 0 : indexCursor;
        String textoCompleto = getText(0, getLength());
        int indexInicioPalabra = ToolsEditor.getIndexInicioPalabra(textoCompleto, indexCursor);
        int indexFinalPalabra = ToolsEditor.getIndexFinPalabra(textoCompleto, indexCursor);
        setStyle2(indexInicioPalabra, indexFinalPalabra);
        
        // System.out.println("//sa lo que sea s\n".matches("^//.*\n$"));
    }
    private void estilizarComentario(int indexCursor) throws BadLocationException {
        String textoCompleto = getText(0, getLength());
    }
    private void comentarioType(int indexCursor, String textoCompleto, String textoIngresado) {
        char posterior, anterior;
        try { posterior = textoCompleto.charAt(indexCursor + 1); } catch (Exception e) { posterior = ' '; }
        try { anterior = textoCompleto.charAt(indexCursor - 1); } catch (Exception e) { anterior = ' '; }
        if (textoCompleto.length() > 1 && textoIngresado.equals("/")) {
            if (posterior == '*') { // Es apertura
                int indexFinComment = textoCompleto.substring(indexCursor).indexOf("*/") + 2;
                if (textoCompleto.substring(indexCursor).indexOf("*/") > -1)
                    setCharacterAttributes(indexCursor, indexFinComment, attribComentarios, false);
                else
                    setCharacterAttributes(indexCursor, textoCompleto.length(), attribComentarios, false);
            }
            else if (anterior == '*') { // Es cierre
                int indexInicioComment = ToolsEditor.getIndexInicioComment(textoCompleto, indexCursor);
                setCharacterAttributes(indexInicioComment, (indexCursor - indexInicioComment) + 1, attribComentarios, false);
            }
            /*else
                setCharacterAttributes(indexCursor, 1, attribComentarios, false);*/
        }
        else if (textoCompleto.length() > 1 && textoIngresado.equals("*")) {
            if (posterior == '/') { // Es cierre
                int indexInicioComment = ToolsEditor.getIndexInicioComment(textoCompleto, indexCursor);
                setCharacterAttributes(indexInicioComment, (indexCursor - indexInicioComment) + 2, attribComentarios, false);
            }
            else if (anterior == '/') { // Es apertura
                int indexFinComment = textoCompleto.substring(indexCursor).indexOf("*/") + 2;
                if (textoCompleto.substring(indexCursor).indexOf("*/") > -1)
                    setCharacterAttributes(indexCursor - 1, indexFinComment + 1, attribComentarios, false);
                else
                    setCharacterAttributes(indexCursor - 1, textoCompleto.length(), attribComentarios, false);
            }
            /*else
                setCharacterAttributes(indexCursor, 1, attribComentarios, false);*/
        }
        else if (ToolsEditor.estaEnComentario(textoCompleto, indexCursor)) {
            int inicio = ToolsEditor.getIndexInicioPalabra(textoCompleto, indexCursor);
            int fin = ToolsEditor.getIndexFinPalabra(textoCompleto, indexCursor);
            setCharacterAttributes(inicio, fin, attribComentarios, false);
        }
    }
    /**
     * Asigna un respectivo estilo a una cadena según los índices
     * @param indexInicioPalabra
     * @param indexFinPalabra
     * @param texto 
     */
    private void setStyle2(int indexInicioPalabra, int indexFinPalabra) throws BadLocationException {
        String textoCompleto = getText(0, getLength());
        AttributeSet attribute = null;
        // if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("^/\\*.*")) // ^ caracter para inicio, $ caracter para final
        //     attribute = attribComentarios;
        // else
        if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("^/\\*.*\\*/$")) // ^ caracter para inicio, $ caracter para final
            attribute = attribComentarios;
        else if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("^//.*\n$")) // ^ caracter para inicio, $ caracter para final
            attribute = attribComentarios;
        else if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("(\\W)*(" + methodClass +")"))
            attribute = attribMethodClass;
        else if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("(\\W)*(" + imports +")"))
            attribute = attribImports;
        else if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("(\\W)*(" + condicionales +")"))
            attribute = attribCondicionales;
        else if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("(\\W)*(" + tipoDatos +")"))
            attribute = attribTipoDatos;
        else if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("^\\$.*"))
            attribute = attribVariables;
        else
            attribute = attribNormal;
        setCharacterAttributes(indexInicioPalabra, indexFinPalabra, attribute, false);
    }
    
    private void estilizarCodigo(int indexCursor, String textoIngresado) throws BadLocationException {
        String textoCompleto = getText(0, getLength());

        int indexCaracterAnteriorAPalabra = getIndexCaracterAnteriorAPalabra(textoCompleto, indexCursor);
        indexCaracterAnteriorAPalabra = indexCaracterAnteriorAPalabra < 0 ? 0 : indexCaracterAnteriorAPalabra;
        // El indexFinPalabraEdit es el índice del cursor al final de la palabra modificada (Ej: "hola" => 4) "Contar la posición donde el cursor se mueve"
        // Si tenemos "Hla como" y modificamos a "Hola como" indexFinPalabraEdit será también 4 porque modificamos esa palabra
        int indexFinPalabraEdit = getIndexFinPalabra(textoCompleto, indexCursor + textoIngresado.length());
        
        
//        System.out.println(indexCaracterAnteriorAPalabra + " -- " + indexFinPalabraEdit);
        
        // Lo siguiente es un filtro para que cuando se encuentre la palabra buscada aplique los atributos instanciados al inicio (attribEspecial, attribNormal)
        int indexPalabraInicio = indexCaracterAnteriorAPalabra;
        int indexPalabraFin = indexCaracterAnteriorAPalabra;
        while (indexPalabraFin <= indexFinPalabraEdit) {
            if (indexPalabraFin == indexFinPalabraEdit || String.valueOf(textoCompleto.charAt(indexPalabraFin)).matches("\\W")) {
                // Aquí hacemos el match con las palabras que necesites si existe alguna entonces se hace un set a los atributos "attribEspecial"
                setStyle(indexPalabraInicio, indexPalabraFin, indexCursor);
                indexPalabraInicio = indexPalabraFin;
            }
            indexPalabraFin++;
        }
    }
    private void estilizarCodigoComment(int indexCursor) throws BadLocationException {
        String textoCompleto = getText(0, getLength());
        int index = 0;
        String textoAVerificar = textoCompleto;
        while (index < textoCompleto.length()) {
            if (textoAVerificar.contains("/*")) {
                index += textoAVerificar.indexOf("/*");
                textoAVerificar = textoCompleto.substring(index);
                int inicio = index;
                int fin = textoCompleto.length() - 1;
                if (textoAVerificar.contains("*/")) {
                    index += textoAVerificar.indexOf("*/") + 2; // Se suma 2 por los caracteres */
                    fin = index;
                    textoAVerificar = textoCompleto.substring(index);
                }
                else {
                    fin = fin + 1;
                    index = fin;
                }
                // System.out.println("verificar(" + inicio + ", " + fin + ") => " + textoCompleto.substring(inicio, fin));
                setStyleComment(inicio, fin, indexCursor);
            }
            else
                index++;
        }

        index = 0;
        textoAVerificar = textoCompleto;
        while (index < textoCompleto.length()) {
            if (textoAVerificar.contains("//")) {
                index += textoAVerificar.indexOf("//");
                textoAVerificar = textoCompleto.substring(index);

                int inicio = index;
                int fin = textoCompleto.length();
                if (textoAVerificar.contains("\n")) {
                    index += textoAVerificar.indexOf("\n") + 1; // Se suma 2 por los caracteres */
                    fin = index;
                    textoAVerificar = textoCompleto.substring(index);
                }
                else {
                    fin = fin + 1;
                    index = fin;
                }
                // System.out.println("verificar => " + textoCompleto.substring(inicio, fin));
                setStyleComment(inicio, fin, indexCursor);
            }
            else
                index++;
        }
    }
    private void estilizarCodigoStrings(int indexCursor) throws BadLocationException {
        String textoCompleto = getText(0, getLength());
        int index = 0;
        String textoAVerificar = textoCompleto;
        while (index < textoCompleto.length()) {
            if (textoAVerificar.contains("\"")) {
                index += textoAVerificar.indexOf("\"");
                textoAVerificar = textoCompleto.substring(index + 1);

                int inicio = index;
                int fin = textoCompleto.length() - 1;
                if (textoAVerificar.contains("\"")) {
                    index += textoAVerificar.indexOf("\"") + 2; // Se suma 2 por los caracteres */
                    fin = index;
                    textoAVerificar = textoCompleto.substring(index);
                }
                else {
                    fin = fin + 1;
                    index = fin;
                }
                // System.out.println("verificar(" + inicio + ", " + fin + ") => " + textoCompleto.substring(inicio, fin));
                setStyleStrings(inicio, fin, indexCursor);
            }
            else
                index++;
        }

        index = 0;
        textoAVerificar = textoCompleto;
        while (index < textoCompleto.length()) {
            if (textoAVerificar.contains("'")) {
                index += textoAVerificar.indexOf("'");
                textoAVerificar = textoCompleto.substring(index + 1);

                int inicio = index;
                int fin = textoCompleto.length() - 1;
                if (textoAVerificar.contains("'")) {
                    index += textoAVerificar.indexOf("'") + 2; // Se suma 2 por los caracteres */
                    fin = index;
                    textoAVerificar = textoCompleto.substring(index);
                }
                else {
                    fin = fin + 1;
                    index = fin;
                }
                // System.out.println("verificar => " + textoCompleto.substring(inicio, fin));
                setStyleStrings(inicio, fin, indexCursor);
            }
            else
                index++;
        }
    }
    /**
     * Asigna un respectivo estilo a una cadena según los índices
     * @param indexInicioPalabra
     * @param indexFinPalabra
     * @param texto 
     */
    private void setStyle(int indexInicioPalabra, int indexFinPalabra, int indexCursor) throws BadLocationException {
        String textoCompleto = getText(0, getLength());
        int suma = ToolsEditor.getCantidadTabsLinea(textoCompleto, indexCursor) > 0 ? 1 : 0;
        suma = 0;
        AttributeSet attribute = null;
        if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("(\\W)*(" + methodClass +")"))
            attribute = attribMethodClass;
        else if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("(\\W)*(" + imports +")"))
            attribute = attribImports;
        else if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("(\\W)*(" + condicionales +")"))
            attribute = attribCondicionales;
        else if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("(\\W)*(" + tipoDatos +")"))
            attribute = attribTipoDatos;
        else if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("^\\$.*"))
            attribute = attribVariables;
        /*else if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("(^\\$.*->.*)"))
            attribute = attribTipoDatos;*/
        else
            attribute = attribNormal;
        setCharacterAttributes(indexInicioPalabra + suma, indexFinPalabra - indexInicioPalabra, attribute, false);
    }
    /**
     * Asigna un respectivo estilo a una cadena según los índices
     * @param indexInicioPalabra
     * @param indexFinPalabra
     * @param texto 
     */
    private void setStyleComment(int indexInicioPalabra, int indexFinPalabra, int indexCursor) throws BadLocationException {
        String textoCompleto = getText(0, getLength());
        int suma = ToolsEditor.getCantidadTabsLinea(textoCompleto, indexCursor) > 0 ? 1 : 0;
        suma = 0;
        AttributeSet attribute = null;

        if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).trim().matches("^//.*")) // ^ caracter para inicio, $ caracter para final
            attribute = attribComentarios;
        // else if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("^/\\*(.*|./\\r\\n.*|.*\\r.*|.*\\n.*)\\*/$")) { // ^ caracter para inicio, $ caracter para final
        else if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).contains("/*"))
            attribute = attribComentarios;
        else
            attribute = attribNormal;
        setCharacterAttributes(indexInicioPalabra + suma, indexFinPalabra - indexInicioPalabra, attribute, false);
    }
    /**
     * Asigna un respectivo estilo a una cadena según los índices
     * @param indexInicioPalabra
     * @param indexFinPalabra
     * @param texto 
     */
    private void setStyleStrings(int indexInicioPalabra, int indexFinPalabra, int indexCursor) throws BadLocationException {
        String textoCompleto = getText(0, getLength());
        int suma = ToolsEditor.getCantidadTabsLinea(textoCompleto, indexCursor) > 0 ? 1 : 0;
        suma = 0;
        AttributeSet attribute = null;

        if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).trim().contains("'")) // ^ caracter para inicio, $ caracter para final
            attribute = attribString;
        // else if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("^/\\*(.*|./\\r\\n.*|.*\\r.*|.*\\n.*)\\*/$")) { // ^ caracter para inicio, $ caracter para final
        else if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).contains("\""))
            attribute = attribString;
        else
            attribute = attribNormal;
        setCharacterAttributes(indexInicioPalabra + suma, indexFinPalabra - indexInicioPalabra, attribute, false);
    }

    @Override
    public void insertString(int indexCursor, String textoIngresado, AttributeSet a) throws BadLocationException {
        super.insertString(indexCursor, textoIngresado, a);
        String textoCompleto = getText(0, getLength());

        
        
        // estilizarCodigo(indexCursor);
        // comentarioType(indexCursor, textoCompleto, textoIngresado);
        
        
        // SINTAXIS
        // if (textoIngresado.length() > 1) {
            estilizarCodigo(indexCursor, textoIngresado);
            estilizarCodigoStrings(indexCursor);
            if (ToolsEditor.existComment(textoCompleto)) {
                estilizarCodigoComment(indexCursor);
            }
            
        /*} // descomentar esto ñuego para que no sea muy lento el programa al escribir (Añade los atributos por cada typeo sin ese if)
        else{
            estilizarCodigo2(indexCursor);
        }*/
    }

    @Override
    public void remove (int indexCursorEliminar, int cantidadCaracteresEliminados) throws BadLocationException {
        super.remove(indexCursorEliminar, cantidadCaracteresEliminados);
        // estilizarCodigo2(indexCursorEliminar - 1);
        estilizarCodigo(indexCursorEliminar, "");
    }
    
    /**
     * Devuelve el índice anterior a la última palabra (Ej: "hola como estas" => 9)
     * @param text
     * @param index
     * @return 
     */
    private int getIndexCaracterAnteriorAPalabra(String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W"))
                break;
        }
        return index;
    }

    /**
     * El indexFinPalabraEdit es el índice del cursor al final de la palabra modificada (Ej: "hola" => 4) "Contar la posición donde el cursor se mueve"
       Si tenemos "Hla como" y modificamos a "Hola como" indexFinPalabraEdit será también 4 porque modificamos esa palabra
     * @param text
     * @param index
     * @return 
     */
    private int getIndexFinPalabra(String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W"))
                break;
            index++;
        }
        return index;
    }
}
