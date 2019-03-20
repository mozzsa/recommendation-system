

import java.io.*;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;

public class ReadData {
    private String myPath;
    private String mySource;

    public ReadData (File file) {
        initRead(file);
    }

    public ReadData (String filename) {
        initRead(filename);
    }

    public CSVParser getCSVParser () {
        return getCSVParser(true);
    }

    public CSVParser getCSVParser (boolean withHeader) {
        return getCSVParser(withHeader, ",");
    }

    public CSVParser getCSVParser (boolean withHeader, String delimiter) {
        if (delimiter == null || delimiter.length() != 1) {
            throw new ResourceException("ReadData: CSV delimiter must be a single character: " + delimiter);
        }
        try {
            char delim = delimiter.charAt(0);
            Reader input = new StringReader(mySource);
            if (withHeader) {
                return new CSVParser(input, CSVFormat.EXCEL.withHeader().withDelimiter(delim));
            }
            else {
                return new CSVParser(input, CSVFormat.EXCEL.withDelimiter(delim));
            }
        }
        catch (Exception e) {
            throw new ResourceException("ReadData: cannot read " + myPath + " as a CSV file.");
        }
    }

    public Iterable<String> getCSVHeaders (CSVParser parser) {
        return parser.getHeaderMap().keySet();
    }
    // Create from a given File
    private void initRead (File f) {
        try {
            initRead(f.getCanonicalPath());
        }
        catch (Exception e) {
            throw new ResourceException("ReadData: cannot access " + f);
        }
    }
    // Create from the name of a File
    private void initRead (String fname) {
        File f = new File(fname);
        try {
            fname = f.getCanonicalPath();
            myPath = fname;
            InputStream is = getClass().getClassLoader().getResourceAsStream(fname);
            if (is == null) {
                is = new FileInputStream(fname);
            }
            mySource = initFromStream(is);
        }
        catch (Exception e) {
            throw new ResourceException("ReadData: cannot access " + fname);
        }
    }
    // store data (keep in sync with URLResource)
    private String initFromStream (InputStream stream) {
        BufferedReader buff = null;
        String line = null;
        try {
            buff = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            StringBuilder contents = new StringBuilder();
            while ((line = buff.readLine()) != null) {
                contents.append(line + "\n");
            }
            return contents.toString();
        }
        catch (Exception e) {
            throw new ResourceException("ReadData: error encountered reading " + myPath, e);
        }
        finally {
            try {
                if (buff != null) {
                    buff.close();
                }
            }
            catch (Exception e) {
                // should never happen
            }
        }
    }

}
