package org.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@SpringBootApplication
class MyApplication {

  @RequestMapping("/")
  fun home(): String {
    return "Hello World!"
  }

}

fun main(args: Array<String>) {
  runApplication<MyApplication>(*args)
}
