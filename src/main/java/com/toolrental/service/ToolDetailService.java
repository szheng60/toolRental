package com.toolrental.service;

import com.toolrental.dao.IToolDetail;
import com.toolrental.model.ToolDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.tools.Tool;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xinyu on 11/5/2017.
 */
@Service
public class ToolDetailService implements IToolDetailService {
    @Autowired
    private IToolDetail toolDetailDao;

    @Override
    public ToolDetail retrieveToolDetail(int toolId) {
        ToolDetail td = toolDetailDao.retrieveToolDetail(toolId);
        td.setFullDescription(reformatFullDescription(td));
        td.setSpecificDescription(reformatSpecification(td));
        return td;
    }

    private String reformatFullDescription(ToolDetail td) {
        String fullDescription = td.getFullDescription();
        if (fullDescription == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(fullDescription);
        Matcher m = Pattern.compile("([0-9]+\\.[0-9]+).*([0-9]+\\.[0-9]+).*").matcher(fullDescription);
        if (m.find(2)) {
            sb = sb.replace(m.start(2), m.end(2), decimalToFraction(m.group(2)));
        }
        if (m.find(1)) {
            sb = sb.replace(m.start(1), m.end(1), decimalToFraction(m.group(1)));
        }
        return sb.toString();
    }

    private String reformatSpecification(ToolDetail td) {
        String specification = td.getSpecificDescription();
        switch(td.getToolSubType()) {
            case "socket":
                return reformatSpecificationForSocket(specification);
            case "ratchet":
                return reformatSpecificationForRatchet(specification);
            case "pruner":
                return reformatSpecificationForPrunning(specification);
            case "digger":
                return reformatSpecificationForDigging(specification);
            case "saw":
                return reformatSpecificationForSaw(specification);
            case "mixer":
                return reformatSpecificationForMixer(specification);
        }
        return specification;
    }

    private String reformatSpecificationForSocket(String s) {
        StringBuilder sb = new StringBuilder(s);
        Matcher m = Pattern.compile(".*([0-9]+\\.[0-9]+).*([0-9]+\\.[0-9]+).*").matcher(s);
        if (m.find(2) && m.group(2) != null) {
            sb = sb.replace(m.start(2), m.end(2), decimalToFraction(m.group(2)));
        }
        if (m.find(1) && m.group(1) != null) {
            sb = sb.replace(m.start(1), m.end(1), decimalToFraction(m.group(1)));
        }
        return sb.toString();
    }

    private String reformatSpecificationForRatchet(String s) {
        StringBuilder sb = new StringBuilder(s);
        Matcher m = Pattern.compile(".*([0-9]+\\.[0-9]+).*").matcher(s);
        if (m.find(1) && m.group(1) != null) {
            sb = sb.replace(m.start(1), m.end(1), decimalToFraction(m.group(1)));
        }
        return sb.toString();
    }

    private String reformatSpecificationForPrunning(String s) {
        StringBuilder sb = new StringBuilder(s);
        Matcher m = Pattern.compile(".*([0-9]+\\.[0-9]+).*").matcher(s);
        if (m.find(1) && m.group(1) != null) {
            sb = sb.replace(m.start(1), m.end(1), decimalToFraction(m.group(1)));
        }
        return sb.toString();
    }
    private String reformatSpecificationForDigging(String s) {
        StringBuilder sb = new StringBuilder(s);
        Matcher m = Pattern.compile(".*blade length - ([0-9]+\\.[0-9]+)(.*([0-9]+\\.[0-9]+).*)?").matcher(s);
        if (m.find(3) && m.group(3) != null) {
            sb = sb.replace(m.start(3), m.end(3), decimalToFraction(m.group(3)));
        }
        if (m.find(1) && m.group(1) != null) {
            sb = sb.replace(m.start(1), m.end(1), decimalToFraction(m.group(1)));
        }
        return sb.toString();
    }

    private String reformatSpecificationForSaw(String s) {
        StringBuilder sb = new StringBuilder(s);
        Matcher m = Pattern.compile(".*([0-9]+\\.[0-9]+).*").matcher(s);
        if (m.find(1) && m.group(1) != null) {
            sb = sb.replace(m.start(1), m.end(1), decimalToFraction(m.group(1)));
        }
        return sb.toString();
    }
    private String reformatSpecificationForMixer(String s) {
        StringBuilder sb = new StringBuilder(s);
        Matcher m = Pattern.compile(".*([0-9]+\\.[0-9]+) HP.*").matcher(s);
        if (m.find(1) && m.group(1) != null) {
            sb = sb.replace(m.start(1), m.end(1), decimalToFraction(m.group(1)));
        }
        return sb.toString();
    }

    private String decimalToFraction(String s) {
        double input = Double.valueOf(s);
        int wholeNumber = (int)Math.floor(input);
        double decimalNumber = input - wholeNumber;
        if (decimalNumber == 0) {
            return String.valueOf(wholeNumber);
        }
        int factor = 1;
        int increment = 10;
        double temp = decimalNumber;
        do {
            factor = increment * factor;
            decimalNumber = temp * factor;
        } while(decimalNumber % 1 != 0);

        int numerator = (int)decimalNumber;
        int denominator = factor;

        int GCD = findGCD(numerator, denominator);
        return (wholeNumber + "-" + numerator/GCD + "/" + denominator/GCD);
    }

    private int findGCD(int number1, int number2) {
        //base case
        if(number2 == 0){
            return number1;
        }
        return findGCD(number2, number1%number2);
    }
}
