package edu.nagojudge.app.business.tools.barcodes;

import java.awt.image.BufferedImage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import edu.nagojudge.msg.pojo.constants.TypeFilesEnum;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;

/**
 *
 * @author andresfelipegarciaduran
 */
public class QRGenerator {

    private final Logger logger = Logger.getLogger(QRGenerator.class);

    public BufferedImage createTextCode(String text, int qrWidth, int qrHeight) throws WriterException {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix encode = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, qrWidth, qrHeight);
            BufferedImage toBufferedImage = MatrixToImageWriter.toBufferedImage(encode);
            return toBufferedImage;
        } catch (WriterException ex) {
            logger.error(ex);
            throw ex;
        }
    }

    public String generateFileImageQRByUser(BufferedImage bufferImage, String fullPathFileQRcode) throws IOException {
        try {
            File outputfile = new File(fullPathFileQRcode);
            ImageIO.write(bufferImage, TypeFilesEnum.PNG.toString(), outputfile);
            return outputfile.getPath();
        } catch (IOException ex) {
            logger.error(ex);
            throw ex;
        }
    }

}
