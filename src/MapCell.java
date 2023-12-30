/**
 * @file   MapCell.java
 * @brief  A lightweight class representing a character on the campus map.
 *         Provides utilities to insert escape codes before and after the 
 *         character so that it is easy to render the whole map.
 *
 * @author Xin Cai
 */
class MapCell {
    char ch = '\0';
    String prefix = "";
    String suffix = "";

    public MapCell(char ch) {
        this.ch = ch;
    }

    /**
     * If the current MapCell's ch field is an open symbol, insert the color
     * escape code at prefix. Otherwise, insert the RESET code at suffix.
     * 
     * @param escapeCode represents a color
     */
    public void setColor(String escapeCode) {
        if (this.ch == '[')
            prefix = escapeCode;
        else
            suffix = Color.RESET.val;
    }

    public void clear() {
        this.prefix = "";
        this.suffix = "";
    }
}
