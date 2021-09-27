package sphynx.tools;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * @author josue
 */
public class TextMarkerPHP {
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
    private int getIndexInicioComment(String text, int index) {
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
    private int getIndexFinComment(String text, int index) {
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
        return -1;
    }
    
    /**
     * Asigna atributos de texto (estilos)
     * @param indexStart Índice inicial
     * @param indexEnd Índice final
     * @param attributeSet Atributos
     */
    /*public void setAttributesText(DefaultStyledDocument a, int indexStart, int indexEnd, AttributeSet attributeSet) {
        a.setCharacterAttributes(indexStart, indexEnd.length(), attributeSet, false);
    }*/

    /**
     * Marca las palabras buscadas en el texto
     */
    public void initMarker(JTextPane textComponent) {
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

        DefaultStyledDocument documentStyle = new DefaultStyledDocument() {
            @Override
            public void insertString(int indexCursorInicial, String textoIngresado, AttributeSet a) throws BadLocationException {
                super.insertString(indexCursorInicial, textoIngresado, a);
                // Texto que contiene el panel de texto
                String textoCompleto = getText(0, getLength());


                // SINTAXIS
                
                // El indexCaracterAnteriorAPalabra es el índice anterior a la última palabra (Ej: "hola como estas" => 9)
                int indexCaracterAnteriorAPalabra = getIndexCaracterAnteriorAPalabra(textoCompleto, indexCursorInicial);
                indexCaracterAnteriorAPalabra = indexCaracterAnteriorAPalabra < 0 ? 0 : indexCaracterAnteriorAPalabra;
                // El indexFinPalabraEdit es el índice del cursor al final de la palabra modificada (Ej: "hola" => 4) "Contar la posición donde el cursor se mueve"
                // Si tenemos "Hla como" y modificamos a "Hola como" indexFinPalabraEdit será también 4 porque modificamos esa palabra
                int indexFinPalabraEdit = getIndexFinPalabra(textoCompleto, indexCursorInicial + textoIngresado.length());
                // Lo siguiente es un filtro para que cuando se encuentre la palabra buscada aplique los atributos instanciados al inicio (attribEspecial, attribNormal)
                int indexPalabraInicio = indexCaracterAnteriorAPalabra;
                int indexPalabraFin = indexCaracterAnteriorAPalabra;
                while (indexPalabraFin <= indexFinPalabraEdit) {
                    int suma = ToolsEditor.getCantidadTabsLinea(textoCompleto, indexCursorInicial) > 0 ? 1 : 0;
                    if (indexPalabraFin == indexFinPalabraEdit || String.valueOf(textoCompleto.charAt(indexPalabraFin)).matches("\\W")) {
                        // Aquí hacemos el match con las palabras que necesites si existe alguna entonces se hace un set a los atributos "attribEspecial"
                        if (textoCompleto.substring(indexPalabraInicio, indexPalabraFin).matches("(\\W)*(" + methodClass +")"))
                            setCharacterAttributes(indexPalabraInicio + suma, indexPalabraFin - indexPalabraInicio, attribMethodClass, false);
                        else if (textoCompleto.substring(indexPalabraInicio, indexPalabraFin).matches("(\\W)*(" + imports +")"))
                            setCharacterAttributes(indexPalabraInicio + suma, indexPalabraFin - indexPalabraInicio, attribImports, false);
                        else if (textoCompleto.substring(indexPalabraInicio, indexPalabraFin).matches("(\\W)*(" + condicionales +")"))
                            setCharacterAttributes(indexPalabraInicio + suma, indexPalabraFin - indexPalabraInicio, attribCondicionales, false);
                        else if (textoCompleto.substring(indexPalabraInicio, indexPalabraFin).matches("(\\W)*(" + tipoDatos +")"))
                            setCharacterAttributes(indexPalabraInicio + suma, indexPalabraFin - indexPalabraInicio, attribTipoDatos, false);
                        else
                            setCharacterAttributes(indexPalabraInicio, indexPalabraFin - indexPalabraInicio, attribNormal, false);
                        indexPalabraInicio = indexPalabraFin;
                    }
                    indexPalabraFin++;
                }
                
                
                
                // COMENTARIOS
                String textComment = textoCompleto;
                int ultimoIndex = 0;
                while (textComment.contains("/*") || textComment.contains("*/")) {
                    int inicioComment = textComment.indexOf("/*");
                    int finComment = textComment.indexOf("*/");

                    if (inicioComment < finComment)
                        setCharacterAttributes(inicioComment + ultimoIndex, finComment + ultimoIndex, attribComentarios, false);
                    else
                        setCharacterAttributes(inicioComment + ultimoIndex, textoCompleto.length(), attribComentarios, false);

                    int aux = ultimoIndex;
                    ultimoIndex += (textComment.indexOf("*/") + 2); // Se suma 2 por el cierre del comentario "*/"
                    if (textComment.contains("*/")) // Tal vez en este if deberia dar los estilos otra vez
                        setCharacterAttributes(ultimoIndex, textComment.length(), attribNormal, false);
                    textComment = textComment.substring((ultimoIndex) - aux);
                }
                
                
            }

            @Override
            public void remove (int indexCursorEliminar, int cantidadCaracteresEliminados) throws BadLocationException {
                super.remove(indexCursorEliminar, cantidadCaracteresEliminados);

                // El procedimiento aquí es similar al que se usó en insertString con la diferencia del filtro para quitar las propiedades a las palabras
                String textoIngresado = getText(0, getLength());
                int indexCaracterAnteriorAPalabra = getIndexCaracterAnteriorAPalabra(textoIngresado, indexCursorEliminar);
                indexCaracterAnteriorAPalabra = indexCaracterAnteriorAPalabra < 0 ? 0 : indexCaracterAnteriorAPalabra;

                int indexFinPalabraElim = getIndexFinPalabra(textoIngresado, indexCursorEliminar);

                if (textoIngresado.substring(indexCaracterAnteriorAPalabra, indexFinPalabraElim).matches("(\\W)*(" + methodClass + ")"))
                    setCharacterAttributes(indexCaracterAnteriorAPalabra, indexFinPalabraElim - indexCaracterAnteriorAPalabra, attribMethodClass, false);
                else if (textoIngresado.substring(indexCaracterAnteriorAPalabra, indexFinPalabraElim).matches("(\\W)*(" + imports + ")"))
                    setCharacterAttributes(indexCaracterAnteriorAPalabra, indexFinPalabraElim - indexCaracterAnteriorAPalabra, attribImports, false);
                else if (textoIngresado.substring(indexCaracterAnteriorAPalabra, indexFinPalabraElim).matches("(\\W)*(" + condicionales + ")"))
                    setCharacterAttributes(indexCaracterAnteriorAPalabra, indexFinPalabraElim - indexCaracterAnteriorAPalabra, attribCondicionales, false);
                else if (textoIngresado.substring(indexCaracterAnteriorAPalabra, indexFinPalabraElim).matches("(\\W)*(" + tipoDatos + ")"))
                    setCharacterAttributes(indexCaracterAnteriorAPalabra, indexFinPalabraElim - indexCaracterAnteriorAPalabra, attribTipoDatos, false);
                else
                    setCharacterAttributes(indexCaracterAnteriorAPalabra, indexFinPalabraElim - indexCaracterAnteriorAPalabra, attribNormal, false);
            }
        };
        textComponent.setDocument(documentStyle);
    }
}
