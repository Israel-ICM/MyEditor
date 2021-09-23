package sphynx.tools;

import java.awt.Font;
import java.io.InputStream;

/**
 * @author Israel-ICM
 */
public class CustomFont {
    private Font font = null;

    public CustomFont() {
        String fontName = "UbuntuMono.ttf" ;
        try {
            InputStream is =  getClass().getResourceAsStream(fontName);
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception ex) {
            //Si existe un error se carga fuente por defecto ARIAL
            System.err.println(fontName + " No se cargo la fuente");
            font = new Font("Arial", Font.PLAIN, 14);            
        }
    }

    /**
     * Font.PLAIN = 0 , Font.BOLD = 1 , Font.ITALIC = 2
     * tamanio = float
     */
    public Font ubuntuMonoSpace(int estilo, float tamanio) {
        Font tfont = font.deriveFont(estilo, tamanio);
        return tfont;
    }
}
