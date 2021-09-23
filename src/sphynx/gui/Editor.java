package sphynx.gui;

import java.awt.Color;
import javafx.scene.input.KeyCode;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import sphynx.tools.CustomFont;
import sphynx.tools.NumeroLinea;

/**
 * @author Israel-ICM
 */
public class Editor extends javax.swing.JFrame {

    public Editor() {
        initComponents();
        setLocationRelativeTo(null);
        NumeroLinea numberLine = new NumeroLinea(txtContentEditor);
        scrollEditorPane.setRowHeaderView(numberLine);
        
        CustomFont cf = new CustomFont();
        txtContentEditor.setFont(cf.ubuntuMonoSpace(1, 14f));
        initMarcadorDeTexto();
        
        // Lo siguiente es un auxiliar para que el tabulador tenga un espacio pequeño (En este caso 30, para cada espacio)
        StyleContext sc = StyleContext.getDefaultStyleContext();
        TabStop[] arrTabs = new TabStop[1000];
        for (int i = 0; i < 1000; i++)
            arrTabs[i] = new TabStop((i + 1) * 30, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
        AttributeSet paraSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.TabSet, new TabSet(arrTabs));
        txtContentEditor.setParagraphAttributes(paraSet, false);
    }
    
    public void inyectarTexto(String text) {
        inyectarTexto(text, false);
    }
    public void inyectarTexto(String text, boolean isEnter) {
        String contenido = txtContentEditor.getText().substring(0, txtContentEditor.getCaretPosition()) + text + txtContentEditor.getText().substring(txtContentEditor.getCaretPosition());
        txtContentEditor.setText(contenido);

        int espaciosRecorrerCursor = -1;
        if (isEnter)
            espaciosRecorrerCursor = -2;
        txtContentEditor.setCaretPosition(txtContentEditor.getCaretPosition() + espaciosRecorrerCursor);
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

    /**
     * Marca las palabras buscadas en el texto
     */
    public void initMarcadorDeTexto() {
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
        txtContentEditor.setDocument(documentStyle);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollEditorPane = new javax.swing.JScrollPane();
        txtContentEditor = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtContentEditor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtContentEditorKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtContentEditorKeyTyped(evt);
            }
        });
        scrollEditorPane.setViewportView(txtContentEditor);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollEditorPane, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollEditorPane, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtContentEditorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContentEditorKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtContentEditor.getText().charAt(txtContentEditor.getCaretPosition() - 1) == '{' && txtContentEditor.getText().charAt(txtContentEditor.getCaretPosition()) == '}') {
                /*String contenido = txtContentEditor.getText().substring(0, txtContentEditor.getCaretPosition()) + "\n\t\n" + txtContentEditor.getText().substring(txtContentEditor.getCaretPosition());
                txtContentEditor.setText(contenido);
                txtContentEditor.setCaretPosition(txtContentEditor.getCaretPosition() - 2);*/
                inyectarTexto("\n\t\n", true);
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtContentEditorKeyPressed

    private void txtContentEditorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContentEditorKeyTyped
        if (evt.getKeyChar() == '{') {
            /*txtContentEditor.setText(txtContentEditor.getText() + "{}");
            txtContentEditor.setCaretPosition(txtContentEditor.getText().length() - 1);*/
            inyectarTexto("{}");
            evt.consume();
        }
        if (evt.getKeyChar() == '(') {
            // txtContentEditor.setText(txtContentEditor.getText() + "()");
            inyectarTexto("()");
            /*String contenido = txtContentEditor.getText().substring(0, txtContentEditor.getCaretPosition()) + "()" + txtContentEditor.getText().substring(txtContentEditor.getCaretPosition());
            txtContentEditor.setText(contenido);
            // txtContentEditor.setCaretPosition(txtContentEditor.getText().length() - 1);
            txtContentEditor.setCaretPosition(txtContentEditor.getCaretPosition() - 1);*/
            evt.consume();
        }
    }//GEN-LAST:event_txtContentEditorKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Editor().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollEditorPane;
    private javax.swing.JTextPane txtContentEditor;
    // End of variables declaration//GEN-END:variables
}
