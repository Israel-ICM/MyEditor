/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sphynx.tools;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author josue
 */
public class TextMarkerJava {
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

    /**
     * Marca las palabras buscadas en el texto
     */
    public void initMarker(JTextPane textComponent) {
        final String palabras = "if|int|float|else|public|class|import|void|extends|implements|static|return|null";
        final StyleContext styleContext = StyleContext.getDefaultStyleContext();
        // Instanciamos los atributos para los estilos para las palabras normales y las que tienes listadas
        // En este caso Azul para las palabras especiales y negro para las normales
        final AttributeSet attribEspecial = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, new Color(0, 102, 153));
        final AttributeSet attribNormal = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        DefaultStyledDocument documentStyle = new DefaultStyledDocument() {
            @Override
            public void insertString(int indexCursorInicial, String textoIngresado, AttributeSet a) throws BadLocationException {
                super.insertString(indexCursorInicial, textoIngresado, a);

                // Texto que contiene el panel de texto
                String textoCompleto = getText(0, getLength());

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
                    if (indexPalabraFin == indexFinPalabraEdit || String.valueOf(textoCompleto.charAt(indexPalabraFin)).matches("\\W")) {
                        // Aquí hacemos el match con las palabras que necesites si existe alguna entonces se hace un set a los atributos "attribEspecial"
                        if (textoCompleto.substring(indexPalabraInicio, indexPalabraFin).matches("(\\W)*(" + palabras +")"))
                            setCharacterAttributes(indexPalabraInicio, indexPalabraFin - indexPalabraInicio, attribEspecial, false);
                        else
                            setCharacterAttributes(indexPalabraInicio, indexPalabraFin - indexPalabraInicio, attribNormal, false);
                        indexPalabraInicio = indexPalabraFin;
                    }
                    indexPalabraFin++;
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

                if (textoIngresado.substring(indexCaracterAnteriorAPalabra, indexFinPalabraElim).matches("(\\W)*(" + palabras + ")")) {
                    setCharacterAttributes(indexCaracterAnteriorAPalabra, indexFinPalabraElim - indexCaracterAnteriorAPalabra, attribEspecial, false);
                } else {
                    setCharacterAttributes(indexCaracterAnteriorAPalabra, indexFinPalabraElim - indexCaracterAnteriorAPalabra, attribNormal, false);
                }
            }
        };
        textComponent.setDocument(documentStyle);
    }
}
