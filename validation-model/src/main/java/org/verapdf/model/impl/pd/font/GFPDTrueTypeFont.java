package org.verapdf.model.impl.pd.font;

import org.verapdf.as.ASAtom;
import org.verapdf.cos.COSArray;
import org.verapdf.cos.COSObjType;
import org.verapdf.cos.COSObject;
import org.verapdf.font.PDFLibFont;
import org.verapdf.font.truetype.AdobeGlyphList;
import org.verapdf.font.truetype.TrueTypeFont;
import org.verapdf.model.factory.operators.RenderingMode;
import org.verapdf.model.pdlayer.PDTrueTypeFont;

/**
 * Represents TrueType font dictionary.
 * //TODO: write doc
 *
 * @author Sergey Shemyakov
 */
public class GFPDTrueTypeFont extends GFPDSimpleFont implements PDTrueTypeFont {

    public static final String TRUETYPE_FONT_TYPE = "PDTrueTypeFont";

    GFPDTrueTypeFont(org.verapdf.pd.PDFont font,
                     RenderingMode renderingMode) {
        super(font, renderingMode, TRUETYPE_FONT_TYPE);
    }

    @Override
    public Boolean getdifferencesAreUnicodeCompliant() {
        PDFLibFont font = this.pdFont.getFontFile();
        if (!(font instanceof TrueTypeFont)) {
            return Boolean.valueOf(false);
        }
        if (!((TrueTypeFont) font).isCmapPresent(3, 1)) {
            return Boolean.valueOf(false);
        }
        COSObject encoding = this.pdFont.getDictionary().getKey(ASAtom.ENCODING);
        if (encoding == COSObject.getEmpty()) {
            return Boolean.valueOf(false);
        }
        COSObject differences = encoding.getKey(ASAtom.DIFFERENCES);
        if (differences.getType() != COSObjType.COS_ARRAY) {
            return Boolean.valueOf(false);
        }
        for (COSObject diff : (COSArray) differences.getDirectBase()) {
            if (diff.getType() == COSObjType.COS_NAME &&
                    !AdobeGlyphList.contains(diff.getString())) {
                return Boolean.valueOf(false);
            }
        }

        return Boolean.valueOf(true);
    }
}
