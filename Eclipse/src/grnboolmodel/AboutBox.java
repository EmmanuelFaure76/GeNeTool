/*
**    Copyright (C) 2010-2014 Emmanuel FAURE, 
**                California Institute of Technology
**                            Pasadena, California, USA.
**
**    This library is free software; you can redistribute it and/or
**    modify it under the terms of the GNU Lesser General Public
**    License as published by the Free Software Foundation; either
**    version 2.1 of the License, or (at your option) any later version.
**
**    This library is distributed in the hope that it will be useful,
**    but WITHOUT ANY WARRANTY; without even the implied warranty of
**    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
**    Lesser General Public License for more details.
**
**    You should have received a copy of the GNU Lesser General Public
**    License along with this library; if not, write to the Free Software
**    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
*/

package grnboolmodel;

import javax.swing.JFrame;
import com.apple.eawt.Application;
import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;

import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class AboutBox extends JFrame implements ActionListener {
    protected JLabel titleLabel, aboutLabel[];
    protected int labelCount = 8;
    protected Font titleFont, bodyFont;
    protected GRNBoolModel p;

    public AboutBox(GRNBoolModel p, BufferedImage icon,int X,int Y,int w,int l) {
        super("");
        this.p = p;
        this.setResizable(false);
        SymWindow aSymWindow = new SymWindow();
        this.addWindowListener(aSymWindow); 
        
        
        /*
        // Initialize useful fonts
        titleFont = new Font("Lucida Grande", Font.BOLD, 18);
        if (titleFont == null)   titleFont = new Font("SansSerif", Font.BOLD, 14);
        bodyFont  = new Font("Lucida Grande", Font.PLAIN, 12);
        if (bodyFont == null)    bodyFont = new Font("SansSerif", Font.PLAIN, 12);
    
        aboutLabel = new JLabel[labelCount];
        aboutLabel[0] = new JLabel("");
        aboutLabel[1] = new JLabel("GRN Boolean Model");
        aboutLabel[1].setFont(titleFont);
        aboutLabel[2] = new JLabel("Version "+Version);
        aboutLabel[2].setFont(bodyFont);
        aboutLabel[3] = new JLabel("");
        aboutLabel[4] = new JLabel("");
        aboutLabel[5] = new JLabel("Emmanuel Faure, Isabelle Peter, Eric H Davidson");
        aboutLabel[5].setFont(bodyFont);
        aboutLabel[6] = new JLabel("faure@caltech.edu");
        aboutLabel[6].setFont(bodyFont);
        aboutLabel[7] = new JLabel("");   
    
        Panel textPanel2 = new Panel(new GridLayout(labelCount, 1));
        for (int i = 0; i<labelCount; i++) {
            aboutLabel[i].setHorizontalAlignment(JLabel.CENTER);
            textPanel2.add(aboutLabel[i]);
        }
     
        this.getContentPane().add (textPanel2, BorderLayout.CENTER);
        */
        this.pack();
        this.setLocation(X,Y);
        this.setSize(w, l+20);
        
         ImageBackgroundPanel imp = new ImageBackgroundPanel(icon);
        this.add(imp);
        
       
    }

    class SymWindow extends java.awt.event.WindowAdapter {
      public void windowClosing(java.awt.event.WindowEvent event) {
        setVisible(false);
      }
    }
    
    public void actionPerformed(ActionEvent newEvent) {
        setVisible(false);
    }   
}


class ImageBackgroundPanel extends JPanel {
    BufferedImage image;
 
    ImageBackgroundPanel(BufferedImage image) {
        this.image = image;
    }
 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}


