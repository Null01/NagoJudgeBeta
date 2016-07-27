/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.tools.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.file.NoSuchFileException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 *
 * @author andresfelipegarciaduran
 */
public class FileUtil implements Serializable {

    private static FileUtil fileUtil;
    private final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static FileUtil getInstance() {
        if (fileUtil == null) {
            fileUtil = new FileUtil();
        }
        return fileUtil;
    }

    public Properties loadFileProperties(String fullPath) throws IOException {
        Properties properties = new Properties();
        InputStream is = null;
        try {
            File file = new File(fullPath);
            is = new FileInputStream(file);
            properties.load(is);
            return properties;
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public void writeFileProperties(String fullPath, String key, String value) throws IOException {
        OutputStream outputStream = null;
        try {
            Properties properties = loadFileProperties(fullPath);
            properties.setProperty(key, value);
            File file = new File(fullPath);
            outputStream = new FileOutputStream(file);
            properties.store(outputStream, null);
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public byte[] parseFromFullPathToArrayByte(String fullPath) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fullPath);
            byte[] inputStreamParseArrayByte = parseFromInputStreamToArrayByte(inputStream);
            return inputStreamParseArrayByte;
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public InputStream parseFromFullPathToInputStream(String fullPath) throws IOException {
        InputStream inputStream = null;
        try {
            File file = new File(fullPath);
            inputStream = new FileInputStream(file);
            return inputStream;
        } catch (FileNotFoundException ex) {
            throw ex;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public byte[] parseFromInputStreamToArrayByte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            int lenght;
            byte[] data = new byte[DEFAULT_BUFFER_SIZE];
            while ((lenght = inputStream.read(data, 0, data.length)) != -1) {
                byteArrayOutputStream.write(data, 0, lenght);
            }
            byteArrayOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
        }
    }

    public void createFile(byte[] content, String pathFile, String nameFile) throws IOException {
        final String fullPath = pathFile + java.io.File.separatorChar + nameFile;
        FileOutputStream fileOuputStream = null;
        try {
            createFolders(pathFile);
            fileOuputStream = new FileOutputStream(fullPath);
            fileOuputStream.write(content);
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (fileOuputStream != null) {
                fileOuputStream.close();
            }
        }
    }

    public void createFolders(String fullPath) throws NoSuchFileException {
        File file = new File(fullPath);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                throw new NoSuchFileException("#NJ - NOSE CREARON LOS DIRECTORIOS [" + fullPath + "]");
            }
        }
    }

    public void createFile(byte[] content, String fullPath) throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fullPath);
            fileOutputStream.write(content);
            fileOutputStream.flush();
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    public boolean existFile(String fullPath) {
        File file = new File(fullPath);
        return file.exists();
    }

    public void removeFile(String fullPath) throws NoSuchFileException {
        File file = new File(fullPath);
        boolean delete = file.delete();
        if (!delete) {
            throw new NoSuchFileException("#NJ - NOSE ELIMINO EL ARCHIVO [" + fullPath + "]");
        }
    }

    public void copyFile(Reader input, Writer output) throws IOException {
        try {
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int n;
            while ((n = input.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }

    public void copyFile(String fullPathFrom, String fullPathTo) throws IOException {
        File sourceFile = new File(fullPathFrom);
        File targetFile = new File(fullPathTo);
        copyDirectory(sourceFile, targetFile);
    }

    public void copyDirectory(File sourceFile, File targetFile) throws IOException {
        if (sourceFile.isDirectory()) {
            if (!targetFile.exists()) {
                targetFile.mkdir();
            }
            String[] childrens = sourceFile.list();
            for (String children : childrens) {
                copyDirectory(new File(sourceFile, children), new File(targetFile, children));
            }
        } else {
            OutputStream outputStream = null;
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(sourceFile);
                outputStream = new FileOutputStream(targetFile);
                byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.flush();

            } catch (FileNotFoundException ex) {
                throw ex;
            } catch (IOException ex) {
                throw ex;
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
    }

    public boolean compareFilesByCheckSumSHA(byte[] content, String checkSum, String TYPE_SHA) throws NoSuchAlgorithmException {
        String checkSumOfFile = generateChechSum(content, TYPE_SHA);
        return checkSumOfFile.compareTo(checkSum) == 0;
    }

    public String generateCheckSum(String pathFile, String nameFile, String TYPE_SHA) throws IOException, NoSuchAlgorithmException {
        String pathComplete = (nameFile != null) ? pathFile + java.io.File.separatorChar + nameFile : pathFile;
        FileInputStream fileInputStream = null;
        try {
            MessageDigest md = MessageDigest.getInstance(TYPE_SHA);
            fileInputStream = new FileInputStream(pathComplete);
            byte[] content = new byte[DEFAULT_BUFFER_SIZE];
            int nread;
            while ((nread = fileInputStream.read(content)) != -1) {
                md.update(content, 0, nread);
            }
            return convertArrayByteToHex(md.digest());
        } catch (NoSuchAlgorithmException ex) {
            throw ex;
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }

    private String generateChechSum(byte[] content, String TYPE_SHA) throws NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance(TYPE_SHA);
            return convertArrayByteToHex(md.digest(content));
        } catch (NoSuchAlgorithmException ex) {
            throw ex;
        }
    }

    public BufferedReader readFile(String pathFile, String nameFile) throws FileNotFoundException {
        try {
            String fullPath = pathFile + ((nameFile != null) ? java.io.File.separatorChar + nameFile : "");
            return new BufferedReader(new FileReader(fullPath));
        } catch (FileNotFoundException ex) {
            throw ex;
        }
    }

    public BufferedReader readFile(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private String convertArrayByteToHex(byte[] digest) {
        StringBuilder outcome = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            outcome.append(Integer.toHexString(0xFF & digest[i]));
        }
        return outcome.toString();
    }

}
