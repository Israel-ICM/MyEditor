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
    final String condicionales = "if|else|elseif|foreach|for|while|return|true|false|null|new|throw|as|=>";
    final String tipoDatos = "int|float|bool|object|stdClass|string|void|&&";
    final StyleContext styleContext = StyleContext.getDefaultStyleContext();
    // Instanciamos los atributos para los estilos para las palabras normales y las que tienes listadas
    // En este caso Azul para las palabras especiales y negro para las normales
    final AttributeSet attribMethodClass = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#6803FF"));
    final AttributeSet attribImports = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#395BBD"));
    final AttributeSet attribCondicionales = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode(/*"#570FEA"*/"#395BBD"));
    final AttributeSet attribTipoDatos = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#B148CE"));
    final AttributeSet attribComentarios = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#C7C7C7"));
    final AttributeSet attribCadenas = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#1E9E51"));
    final AttributeSet attribNormal = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
    final AttributeSet prueba = styleContext.getEmptySet();

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
    private void estilizarCodigo(int indexCursor) throws BadLocationException {
        indexCursor = indexCursor < 0 ? 0 : indexCursor;
        String textoCompleto = getText(0, getLength());
        int indexInicioPalabra = ToolsEditor.getIndexInicioPalabra(textoCompleto, indexCursor);
        int indexFinalPalabra = ToolsEditor.getIndexFinPalabra(textoCompleto, indexCursor);
        setStyle(indexInicioPalabra, indexFinalPalabra);
        
        // System.out.println("//sa lo que sea s\n".matches("^//.*\n$"));
    }
    private void estilizarComentario(int indexCursor) throws BadLocationException {
        String textoCompleto = getText(0, getLength());
    }
    /**
     * Asigna un respectivo estilo a una cadena según los índices
     * @param indexInicioPalabra
     * @param indexFinPalabra
     * @param texto 
     */
    private void setStyle(int indexInicioPalabra, int indexFinPalabra) throws BadLocationException {
        String textoCompleto = getText(0, getLength());
        AttributeSet attribute = null;
        /*if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("^/\\*.*")) // ^ caracter para inicio, $ caracter para final
            attribute = attribComentarios;
        else*/ if (textoCompleto.substring(indexInicioPalabra, indexFinPalabra).matches("^/\\*.*\\*/$")) // ^ caracter para inicio, $ caracter para final
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
        else
            attribute = attribNormal;
        setCharacterAttributes(indexInicioPalabra, indexFinPalabra, attribute, false);
    }

    @Override
    public void insertString(int indexCursor, String textoIngresado, AttributeSet a) throws BadLocationException {
        super.insertString(indexCursor, textoIngresado, a);
        String textoCompleto = getText(0, getLength());

        
        
        
        estilizarCodigo(indexCursor);
        
        
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

    @Override
    public void remove (int indexCursorEliminar, int cantidadCaracteresEliminados) throws BadLocationException {
        super.remove(indexCursorEliminar, cantidadCaracteresEliminados);
        estilizarCodigo(indexCursorEliminar - 1);
    }
}
