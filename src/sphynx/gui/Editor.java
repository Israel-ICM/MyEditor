package sphynx.gui;

import java.awt.Color;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import sphynx.tools.CustomFont;
import sphynx.tools.NumeroLinea;
import sphynx.tools.TextMarkerPHP;
import sphynx.tools.ToolsEditor;

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
        new TextMarkerPHP().initMarker(txtContentEditor);
        
        // Lo siguiente es un auxiliar para que el tabulador tenga un espacio pequeño (En este caso 30, para cada espacio)
        StyleContext sc = StyleContext.getDefaultStyleContext();
        TabStop[] arrTabs = new TabStop[1000];
        for (int i = 0; i < 1000; i++)
            arrTabs[i] = new TabStop((i + 1) * 30, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
        AttributeSet paraSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.TabSet, new TabSet(arrTabs));
        txtContentEditor.setParagraphAttributes(paraSet, false);
        
        txtContentEditor.setBackground(Color.decode("#F4F4F4"));
        txtContentEditor.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                // System.out.println("aqui esta: " + e.getMark());
            }
        });
    }
    
    /**
     * Inyecta texto en el lugar donde se encuentra el cursor
     * @param text Texto completo del input
     */
    public void inyectarTexto(String text) {
        inyectarTexto(text, 0);
    }
    /**
     * Inyecta texto en el lugar donde se encuentra el cursor
     * @param text Texto completo del input
     * @param recorrerCursorIzquierda Cuantos caracteres se recorrera el cursor a la izquierda
     */
    public void inyectarTexto(String text, int recorrerCursorIzquierda) {
        try {
            txtContentEditor.getStyledDocument().insertString(txtContentEditor.getCaretPosition(), text, null);
            int espaciosRecorrerCursor = -(1 + recorrerCursorIzquierda);
            txtContentEditor.setCaretPosition(txtContentEditor.getCaretPosition() + espaciosRecorrerCursor);
        } catch (BadLocationException ex) {
            System.out.println("inyectarTexto(): " + ex.getMessage());
        }
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
            int numTabs = ToolsEditor.getCantidadTabsLinea(txtContentEditor.getText(), txtContentEditor.getCaretPosition());
            String tabs = ToolsEditor.repeatText("\t", numTabs);

            // Hay que eliminar los saltos para hacer un correcto calculo de la posición del cursor
            String textSinSaltos = txtContentEditor.getText().replaceAll("\n", "");
            if (txtContentEditor.getText().length() > 0) {
                if (textSinSaltos.charAt(txtContentEditor.getCaretPosition() - 1) == '{' && textSinSaltos.charAt(txtContentEditor.getCaretPosition()) == '}') {
                    inyectarTexto("\n" + tabs + "\t\n" + tabs, numTabs);
                    evt.consume();
                }
                else if (textSinSaltos.charAt(txtContentEditor.getCaretPosition() - 1) == '[' && textSinSaltos.charAt(txtContentEditor.getCaretPosition()) == ']') {
                    inyectarTexto("\n" + tabs + "\t\n" + tabs, numTabs);
                    evt.consume();
                }
                /*else { // Si el enter se dá en un lugar que no sea al medio de un caracter de apertura y cierre
                    // if (textSinSaltos.charAt(txtContentEditor.getCaretPosition() - 1) == '\t')
                    System.out.println("llega ver: " + numTabs);
                    inyectarTexto("\n" + tabs, -numTabs);
                    evt.consume();
                }*/
            }
        }
    }//GEN-LAST:event_txtContentEditorKeyPressed

    private void txtContentEditorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContentEditorKeyTyped
        String text = txtContentEditor.getText().replaceAll("\n", "");
        if (text.length() > 0) {
            try {
                if (evt.getKeyChar() == ']' && text.charAt(txtContentEditor.getCaretPosition() - 1) == '[') {
                    txtContentEditor.setCaretPosition(txtContentEditor.getCaretPosition() + 1);
                    evt.consume();
                    return;
                }
                else if (evt.getKeyChar() == ')' && text.charAt(txtContentEditor.getCaretPosition() - 1) == '(') {
                    txtContentEditor.setCaretPosition(txtContentEditor.getCaretPosition() + 1);
                    evt.consume();
                    return;
                }
                else if (evt.getKeyChar() == '}' && text.charAt(txtContentEditor.getCaretPosition() - 1) == '{') {
                    txtContentEditor.setCaretPosition(txtContentEditor.getCaretPosition() + 1);
                    evt.consume();
                    return;
                }
                else if (evt.getKeyChar() == '\'' && text.charAt(txtContentEditor.getCaretPosition() - 1) == '\'' && text.charAt(txtContentEditor.getCaretPosition()) == '\'') {
                    txtContentEditor.setCaretPosition(txtContentEditor.getCaretPosition() + 1);
                    evt.consume();
                    return;
                }
                else if (evt.getKeyChar() == '"' && text.charAt(txtContentEditor.getCaretPosition() - 1) == '"' && text.charAt(txtContentEditor.getCaretPosition()) == '"') {
                    txtContentEditor.setCaretPosition(txtContentEditor.getCaretPosition() + 1);
                    evt.consume();
                    return;
                }
            }
            catch(Exception e) {
                // Este catch se hizo principalmente para las comillas por que su validacion en algunos casos son índices fuera de rango
            }
        }
        
        switch (evt.getKeyChar()) {
            case '{':
                inyectarTexto("{}");
                evt.consume();
                break;
            case '(':
                inyectarTexto("()");
                evt.consume();
                break;
            case '[':
                inyectarTexto("[]");
                evt.consume();
                break;
            case '\'':
                inyectarTexto("''");
                evt.consume();
                break;
            case '\"':
                inyectarTexto("\"\"");
                evt.consume();
                break;
            default:
                break;
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
