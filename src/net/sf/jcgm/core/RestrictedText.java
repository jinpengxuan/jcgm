/*
 * <copyright> Copyright 1997-2003 BBNT Solutions, LLC under sponsorship of the
 * Defense Advanced Research Projects Agency (DARPA).
 * Copyright 2009 Swiss AviationSoftware Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the Cougaar Open Source License as published by DARPA on
 * the Cougaar Open Source Website (www.cougaar.org).
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.sf.jcgm.core;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.*;

import net.sf.jcgm.core.TextAlignment.HorizontalAlignment;
import net.sf.jcgm.core.TextAlignment.VerticalAlignment;


/**
 * Class=4, Element=5
 * @author xphc (Philippe Cadé)
 * @author BBNT Solutions
 * @version $Id: RestrictedText.java,v 1.7 2009-05-29 11:04:13 xphc Exp $
 */
class RestrictedText extends Command {
    private String string;
	private double deltaWidth;
	private double deltaHeight;
	private Point2D.Double textPosition;
	private RestrictedTextType.Type type;

	private Shape shape;

    public RestrictedText(int ec, int eid, int l, DataInput in)
            throws IOException {
        super(ec, eid, l, in);

        this.deltaWidth = makeVdc();
        this.deltaHeight = makeVdc();
        this.textPosition = makePoint();
        
        int finalNotFinal = makeEnum();
        
        this.string = makeString();
        
        this.type = RestrictedTextType.getType();

        // make sure all the arguments were read
        assert (this.currentArg == this.args.length);
    }

    @Override
	public String toString() {
        return "RestrictedText \"" + this.string + "\" deltaWidth=" + this.deltaWidth +
        " deltaHeight=" + this.deltaHeight + " textPosition.x=" + this.textPosition.x + 
        " textPosition.y=" + this.textPosition.y;
        
    }

	private Point2D.Double getTextPosition(CGMDisplay d) {
        // the location of the bounding box depends on the alignment and the text path
		TextPath.Type textPath = d.getTextPath();
		
		if (TextPath.Type.UP.equals(textPath)) {
	        double xPos = 0;
	        HorizontalAlignment horizontalAlignment = d.getHorizontalTextAlignment();
	        if (HorizontalAlignment.NORMAL_HORIZONTAL.equals(horizontalAlignment)) {
	        	xPos = -this.deltaWidth / 2;
	        }
	        else if (HorizontalAlignment.LEFT.equals(horizontalAlignment)) {
	        	xPos = 0;
	        }
	        else if (HorizontalAlignment.CENTRE.equals(horizontalAlignment)) {
	        	xPos = -this.deltaWidth/2;
	        }
	        else if (HorizontalAlignment.RIGHT.equals(horizontalAlignment)) {
	        	xPos = -this.deltaWidth;
	        }
	        else if (HorizontalAlignment.CONTINOUS_HORIZONTAL.equals(horizontalAlignment)) {
	        	xPos = -this.deltaWidth * d.getContinuousHorizontalAlignment();
	        }
	        
	        double yPos = 0;
	        VerticalAlignment verticalAlignment = d.getVerticalTextAlignment();
	        if (VerticalAlignment.NORMAL_VERTICAL.equals(verticalAlignment) ||
	        		VerticalAlignment.BASE.equals(verticalAlignment)) {
	        	yPos = 0;
	        }
	        else if (VerticalAlignment.TOP.equals(verticalAlignment)) {
	        	yPos = this.deltaHeight;
	        }
	        else if (VerticalAlignment.CAP.equals(verticalAlignment)) {
	        	yPos = this.deltaHeight;
	        }
	        else if (VerticalAlignment.HALF.equals(verticalAlignment)) {
	        	yPos = this.deltaHeight/2;
	        }
	        else if (VerticalAlignment.CONTINOUS_VERTICAL.equals(verticalAlignment)) {
	        	yPos = this.deltaHeight * d.getContinuousVerticalAlignment();
	        }
	        return new Point2D.Double(xPos, yPos);
		}
		
		if (TextPath.Type.DOWN.equals(textPath)) {
	        double xPos = 0;
	        HorizontalAlignment horizontalAlignment = d.getHorizontalTextAlignment();
	        if (HorizontalAlignment.NORMAL_HORIZONTAL.equals(horizontalAlignment)) {
	        	xPos = -this.deltaWidth / 2;
	        }
	        else if (HorizontalAlignment.LEFT.equals(horizontalAlignment)) {
	        	xPos = 0;
	        }
	        else if (HorizontalAlignment.CENTRE.equals(horizontalAlignment)) {
	        	xPos = -this.deltaWidth/2;
	        }
	        else if (HorizontalAlignment.RIGHT.equals(horizontalAlignment)) {
	        	xPos = -this.deltaWidth;
	        }
	        else if (HorizontalAlignment.CONTINOUS_HORIZONTAL.equals(horizontalAlignment)) {
	        	xPos = -this.deltaWidth * d.getContinuousHorizontalAlignment();
	        }
	        
	        double yPos = 0;
	        VerticalAlignment verticalAlignment = d.getVerticalTextAlignment();
	        if (VerticalAlignment.NORMAL_VERTICAL.equals(verticalAlignment)) {
	        	yPos = this.deltaHeight;
	        }
	        else if (VerticalAlignment.BASE.equals(verticalAlignment)) {
	        	yPos = 0;
	        }
	        else if (VerticalAlignment.TOP.equals(verticalAlignment)) {
	        	yPos = this.deltaHeight;
	        }
	        else if (VerticalAlignment.CAP.equals(verticalAlignment)) {
	        	yPos = this.deltaHeight;
	        }
	        else if (VerticalAlignment.HALF.equals(verticalAlignment)) {
	        	yPos = this.deltaHeight/2;
	        }
	        else if (VerticalAlignment.CONTINOUS_VERTICAL.equals(verticalAlignment)) {
	        	yPos = this.deltaHeight * d.getContinuousVerticalAlignment();
	        }
	        return new Point2D.Double(xPos, yPos);
		}
		
		if (TextPath.Type.LEFT.equals(textPath)) {
	        double xPos = 0;
	        HorizontalAlignment horizontalAlignment = d.getHorizontalTextAlignment();
	        if (HorizontalAlignment.NORMAL_HORIZONTAL.equals(horizontalAlignment)) {
	        	xPos = -this.deltaWidth;
	        }
	        else if (HorizontalAlignment.LEFT.equals(horizontalAlignment)) {
	        	xPos = 0;
	        }
	        else if (HorizontalAlignment.CENTRE.equals(horizontalAlignment)) {
	        	xPos = -this.deltaWidth/2;
	        }
	        else if (HorizontalAlignment.RIGHT.equals(horizontalAlignment)) {
	        	xPos = -this.deltaWidth;
	        }
	        else if (HorizontalAlignment.CONTINOUS_HORIZONTAL.equals(horizontalAlignment)) {
	        	xPos = -this.deltaWidth * d.getContinuousHorizontalAlignment();
	        }
	        
	        double yPos = 0;
	        VerticalAlignment verticalAlignment = d.getVerticalTextAlignment();
	        if (VerticalAlignment.NORMAL_VERTICAL.equals(verticalAlignment) ||
	        		VerticalAlignment.BASE.equals(verticalAlignment)) {
	        	yPos = 0;
	        }
	        else if (VerticalAlignment.TOP.equals(verticalAlignment)) {
	        	yPos = this.deltaHeight;
	        }
	        else if (VerticalAlignment.CAP.equals(verticalAlignment)) {
	        	// TODO
	        }
	        else if (VerticalAlignment.HALF.equals(verticalAlignment)) {
	        	yPos = this.deltaHeight/2;
	        }
	        else if (VerticalAlignment.CONTINOUS_VERTICAL.equals(verticalAlignment)) {
	        	yPos = this.deltaHeight * d.getContinuousVerticalAlignment();
	        }
	        return new Point2D.Double(xPos, yPos);
		}
		
        double xPos = 0;
        HorizontalAlignment horizontalAlignment = d.getHorizontalTextAlignment();
        if (HorizontalAlignment.NORMAL_HORIZONTAL.equals(horizontalAlignment)) {
        	xPos = 0;
        }
        else if (HorizontalAlignment.LEFT.equals(horizontalAlignment)) {
        	xPos = 0;
        }
        else if (HorizontalAlignment.CENTRE.equals(horizontalAlignment)) {
        	xPos = -this.deltaWidth/2;
        }
        else if (HorizontalAlignment.RIGHT.equals(horizontalAlignment)) {
        	xPos = -this.deltaWidth;
        }
        else if (HorizontalAlignment.CONTINOUS_HORIZONTAL.equals(horizontalAlignment)) {
        	xPos = -this.deltaWidth * d.getContinuousHorizontalAlignment();
        }
        
        double yPos = 0;
        VerticalAlignment verticalAlignment = d.getVerticalTextAlignment();
        if (VerticalAlignment.NORMAL_VERTICAL.equals(verticalAlignment) ||
        		VerticalAlignment.BASE.equals(verticalAlignment)) {
        	yPos = 0;
        }
        else if (VerticalAlignment.TOP.equals(verticalAlignment)) {
        	yPos = this.deltaHeight;
        }
        else if (VerticalAlignment.CAP.equals(verticalAlignment)) {
        	// TODO
        }
        else if (VerticalAlignment.HALF.equals(verticalAlignment)) {
        	yPos = this.deltaHeight/2;
        }
        else if (VerticalAlignment.CONTINOUS_VERTICAL.equals(verticalAlignment)) {
        	yPos = this.deltaHeight * d.getContinuousVerticalAlignment();
        }
        return new Point2D.Double(xPos, yPos);
	}

	/**
	 * @param glyphVector
	 */
	private void applyTextPath(CGMDisplay d, GlyphVector glyphVector) {
		double height = glyphVector.getLogicalBounds().getHeight();
		
		if (TextPath.Type.DOWN.equals(d.getTextPath())) {
			float[] glyphPositions = glyphVector.getGlyphPositions(0, glyphVector.getNumGlyphs(), null);
			
			int glyphIndex = 0;
			for (int i = 0; i < (glyphPositions.length / 2); i++) {
				Point2D.Float newPos = new Point2D.Float(glyphPositions[0], (float)(i*height));
				glyphVector.setGlyphPosition(glyphIndex++, newPos);
			}
		}
		else if (TextPath.Type.DOWN.equals(d.getTextPath())) {
			
		}
	}

	/**
	 * Flip the given string for left text path
	 * @param s
	 * @return
	 */
	private String flipString(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = s.length() - 1; i >= 0; i--) {
			sb.append(s.charAt(i));
		}
		return sb.toString();
	}
	
	private void scaleText(CGMDisplay d, FontMetrics fontMetrics, GlyphVector glyphVector, double width, double height) {
		Graphics2D g2d = d.getGraphics2D();
		
		double scaleX = 1;
		double scaleY = 1;
		
		if (TextPath.Type.DOWN.equals(d.getTextPath()) || TextPath.Type.UP.equals(d.getTextPath())) {
			Point2D glyphPosition = glyphVector.getGlyphPosition(1);
			scaleX = this.deltaWidth / glyphPosition.getX();
			scaleY = this.deltaHeight / 100;
		}
		else {
			if (this.type.equals(RestrictedTextType.Type.BASIC)) {
				// only use the character height and do an even scaling
				scaleX = width != 0 ? this.deltaWidth / width : 1;
				scaleY = height != 0 ? this.deltaHeight / height : 1;
			}
			else if (this.type.equals(RestrictedTextType.Type.BOXED_ALL)) {
				scaleX = width != 0 ? this.deltaWidth / width : 1;
				scaleY = height != 0 ? this.deltaHeight / height : 1;
			}
			else if (this.type.equals(RestrictedTextType.Type.BOXED_CAP)) {
				//height -= fontMetrics.getDescent();
				scaleX = width != 0 ? this.deltaWidth / width : 1;
				scaleY = height != 0 ? this.deltaHeight / height : 1;
			}
			else if (this.type.equals(RestrictedTextType.Type.ISOTROPIC_ALL)) {
				double min = Math.min(width != 0 ? this.deltaWidth / width : 1, height != 0 ? this.deltaHeight /
						height : 1);
				scaleX = scaleY = min;
			}
			else if (this.type.equals(RestrictedTextType.Type.ISOTROPIC_CAP)) {
				height -= fontMetrics.getDescent();
				double min = Math.min(width != 0 ? this.deltaWidth / width : 1, height != 0 ? this.deltaHeight /
						height : 1);
				scaleX = scaleY = min;
			}
			else if (this.type.equals(RestrictedTextType.Type.JUSTIFIED)) {
			}
		}
		
		g2d.scale(scaleX, scaleY);
	}
	
	@Override
	public void paint(CGMDisplay d) {
    	if (this.string.length() == 0) {
    		// ignore empty strings
    		return;
    	}
    	
		Graphics2D g2d = d.getGraphics2D();
		
		// save the transformation since we are going to apply another one that
		// is specific to this string
        AffineTransform savedTransform = g2d.getTransform();
        
        AffineTransform coordinateSystemTransformation = d.getCoordinateSystemTransformation(
        	this.textPosition,
        	d.getCharacterOrientationBaselineVector(), d.getCharacterOrientationUpVector());
        
        AffineTransform textTransform = d.getTextTransform();
        coordinateSystemTransformation.concatenate(textTransform);

        g2d.transform(coordinateSystemTransformation);
        
        Point2D.Double textOrigin = getTextPosition(d);
        g2d.translate(textOrigin.x, textOrigin.y);

        // DEBUG: draw the outline
//        g2d.setColor(Color.MAGENTA);
//        g2d.draw(new Rectangle2D.Double(0, -this.deltaHeight,
//        	this.deltaWidth, this.deltaHeight));
        
        g2d.setColor(d.getTextColor());
        
    	String decodedString = d.useSymbolEncoding() ?
        		SymbolDecoder.decode(this.string) : this.string;
        		
        // the text path left is easy: just flip the string
        if (TextPath.Type.LEFT.equals(d.getTextPath())) {
        	decodedString = flipString(decodedString);
        }
        		
        Font font = g2d.getFont();
        
        // adjust the size of the font depending on the extent. If the extent is
		// very big, having small font sizes may create problems
        Point2D.Double[] extent = d.getExtent();
        Font adjustedFont = font.deriveFont((float) (Math.abs(extent[0].y - extent[1].y) / 100));
        g2d.setFont(adjustedFont);
    	FontRenderContext fontRenderContext = g2d.getFontRenderContext();
		GlyphVector glyphVector = adjustedFont.createGlyphVector(fontRenderContext, decodedString);
		Rectangle2D logicalBounds = glyphVector.getLogicalBounds();
		
    	FontMetrics fontMetrics = g2d.getFontMetrics(adjustedFont);
		// XXX: unfortunately, getAscent() does not return correct values, 
		// see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6623223
		// so we are always going to be a bit off
		int screenResolution;
		if (GraphicsEnvironment.isHeadless()) {
			// if we're in a headless environment, assume 96 dots per inch
			// (default setting for Windows XP)
			screenResolution = 96;
		}
		else {
			screenResolution = Toolkit.getDefaultToolkit().getScreenResolution();
		}
    	double height = fontMetrics.getAscent() * 72 / screenResolution;
		
    	scaleText(d, fontMetrics, glyphVector, logicalBounds.getWidth(), height);
    	
    	if (TextPath.Type.UP.equals(d.getTextPath()) || TextPath.Type.DOWN.equals(d.getTextPath())) {
    		applyTextPath(d, glyphVector);
    	}
		
		g2d.drawGlyphVector(glyphVector, 0, 0);
		
        // restore the transformation that existed before painting the string
        g2d.setTransform(savedTransform);
    }

}

/*
 * vim:encoding=utf8
 */
