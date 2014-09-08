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

public class JavaMacDock
{  
  public  BufferedImage icon;
  public int X,Y,w,l;
  private GRNBoolModel p;
  
  public JavaMacDock(GRNBoolModel p, BufferedImage icon,int X,int Y,int w,int l)
  {
    this.p = p;
    this.icon=icon;
    this.X=X;this.Y=Y;this.w=w;this.l=l;
    // create an instance of the mac osx Application class
    Application theApplication = new Application();
    
    // create an instance of our DockBarAdapter class (see source code below)
    DockBarAdapter dockBarAdapter = new DockBarAdapter(p, this);

    // add our adapter as a listener on the application object
    theApplication.addApplicationListener(dockBarAdapter);
  }
  
  // our "callback" method. this method is called by the DockBarAdapter
  // when a "handleOpenFile" event is received.
  public void handleOpenFileEvent(ApplicationEvent e)
  {
     p.println(" Drop " + e.getFilename());
  }
  
}


class DockBarAdapter extends ApplicationAdapter
{
  private JavaMacDock handler;
  private AboutBox aboutBox;
  private GRNBoolModel p;
  // the main class passes a reference to itself to us when we are constructed
  public DockBarAdapter(GRNBoolModel p, JavaMacDock handler)
  {
    this.p = p;
    this.handler = handler;
  }
  
  // this is the method that is called when a drag and drop event is received
  // by the Application, and passed to us. In turn, we call back to the main
  // class to let it know this event was received so it can deal with the
  // event.
  public void handleOpenFile(ApplicationEvent e)
  {
    handler.handleOpenFileEvent(e);
  }
  public void handleQuit(ApplicationEvent e)
  {
    p.exit();
  }
  
  public void handleAbout(ApplicationEvent event) {
        if (aboutBox == null)   aboutBox = new AboutBox(p, handler.icon,handler.X,handler.Y,handler.w,handler.l);
        aboutBox.setResizable(false);
  aboutBox.setVisible(true);
        event.setHandled(true);
    }
}

