package igloo.rest.controller;

import igloo.rest.domain.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yikai Gong
 */

@Controller
public class ViewController {
    @RequestMapping(value="/", method= RequestMethod.GET)
    public String index() {
        return "index.html";
    }

    @RequestMapping(value="/a", method= RequestMethod.GET)
    public String test() {
        return "test.html";
    }

    @RequestMapping(value="/a", method= RequestMethod.POST)
    public @ResponseBody ResponseEntity<Test> test2(@RequestBody Test test) {
        System.out.println("!!");
        return new ResponseEntity<Test>(test, HttpStatus.OK);
    }

    @RequestMapping(value="/t3", method= RequestMethod.GET)
    public @ResponseBody ResponseEntity<Test> test3(@RequestParam String firstname, @RequestParam String lastname) {
        System.out.println(firstname);
        System.out.println(lastname);
        return new ResponseEntity<Test>(new Test("yikai","gong"), HttpStatus.OK);
    }

}
