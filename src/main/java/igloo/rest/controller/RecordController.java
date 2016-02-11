package igloo.rest.controller;

import com.google.gson.JsonArray;
import igloo.database.CustomService;
import igloo.database.entity.RecordEntity;
import igloo.database.repository.RecordRepository;
import igloo.rest.domain.OperationHoursPerMonth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yikai Gong
 */
@Controller
@RequestMapping(value = "/api")
public class RecordController {
    @Autowired
    RecordRepository recordRepository;

    @Autowired
    CustomService customService;

    @RequestMapping(value="/records/all", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<RecordEntity>> getAllRecords() {
        List<RecordEntity> records = recordRepository.findAllRecords();
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @RequestMapping(value="/records/{serialNum}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<RecordEntity>> getAllRecordsBySerialNum(@PathVariable String serialNum) {
        List<RecordEntity> records = recordRepository.findRecordsBySerialNum(serialNum);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @RequestMapping(value="/recordsByChip/{chipId}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<RecordEntity>> getAllRecordsByChipId(@PathVariable String chipId) {
        List<RecordEntity> records = recordRepository.findRecordsByChipId(chipId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @RequestMapping(value="/devices/all", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<RecordEntity>> getAllDevices
            (@RequestParam(value="latiMin", required=false, defaultValue="-90")Double latiMin,
             @RequestParam(value="latiMax", required=false, defaultValue="90")Double latiMax,
             @RequestParam(value="lonMin", required=false, defaultValue="-180")Double lonMin,
             @RequestParam(value="lonMax", required=false, defaultValue="180")Double lonMax)
    {
        List<RecordEntity> records = null;
//        if(latiMin.equals("")||latiMax.equals("")||lonMin.equals("")||lonMax.equals(""))
//            records = recordRepository.findAllDevices();
//        else
            records = recordRepository.findAllDevicesByBoundingBox(latiMin, latiMax, lonMin, lonMax);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @RequestMapping(value="/records/operationHour/{chipId}/{startDate}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> getOperationHourChipId(@PathVariable String chipId, @PathVariable String startDate) {
        JsonArray jsonArray = customService.getOperationHourPerDayStartat(chipId, startDate);
//        System.out.println(jsonArray.toString());
        return new ResponseEntity<>(jsonArray.toString(), HttpStatus.OK);
    }

    @RequestMapping(value="/records/operationHourPerMonth/{chipId}/{year}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<OperationHoursPerMonth>> getOperationHourChipIdByYear(@PathVariable String chipId, @PathVariable Integer year) {
        List<OperationHoursPerMonth> operationHoursPerMonthList = recordRepository.findOperationHoursPerMonth(chipId, year);
        return new ResponseEntity<>(operationHoursPerMonthList, HttpStatus.OK);
    }

    @RequestMapping(value="/recordsByChip/{chipId}", method=RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Boolean> deleteAllRecordsByChipId(@PathVariable String chipId) {
        recordRepository.deleteRecordsByChipId(chipId);
        return new ResponseEntity<>(new Boolean(true), HttpStatus.OK);
    }
}


//List<OperationHoursPerMonth>