/*
**    Copyright (C) 2014 Institute for Systems Biology 
**                       Seattle, Washington, USA. 
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
**    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

//
// Yes, using the default package is bogus, but that is the way that Processing likes it!
//

import processing.core.PApplet;

public class GeNeTool {
  public static void main(final String[] array) {
    String[] array2 = { "grnboolmodel.GRNBoolModel" };
    if (array != null) {
      PApplet.main(PApplet.concat(array2, array));
    } else {
      PApplet.main(array2);
    }
  }
}
