/*
 * Copyright (c) 2009, Swiss AviationSoftware Ltd. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * - Neither the name of the Swiss AviationSoftware Ltd. nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.sf.jcgm.image.loader.cgm;

import static net.sf.jcgm.core.MIMETypes.CGM_MIME_Types;

import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageLoaderFactory;
import org.apache.xmlgraphics.image.loader.spi.ImageLoader;

/**
 * Factory class for the ImageLoader for CGM.
 * @author xphc (Philippe Cadé)
 * @version $Id: ImageLoaderFactoryCGM.java,v 1.1 2009-04-14 14:34:16 xphc Exp $
 * @since Apr 14, 2009
 * @see ImageLoaderCGM
 * @see <a href="http://xmlgraphics.apache.org/">xmlgraphics.apache.org</a>
 */
public class ImageLoaderFactoryCGM extends AbstractImageLoaderFactory {

    private static final ImageFlavor[] FLAVORS = new ImageFlavor[] {
        ImageFlavor.GRAPHICS2D};
    
	@Override
	public ImageFlavor[] getSupportedFlavors(String mime) {
		return FLAVORS;
	}

	@Override
	public String[] getSupportedMIMETypes() {
		return CGM_MIME_Types;
	}

	@Override
	public int getUsagePenalty(String mime, ImageFlavor flavor) {
		return 0;
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public ImageLoader newImageLoader(ImageFlavor targetFlavor) {
		return new ImageLoaderCGM(targetFlavor);
	}

}
