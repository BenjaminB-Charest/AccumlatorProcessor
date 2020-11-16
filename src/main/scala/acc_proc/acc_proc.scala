package acc_proc

import chisel3._
import chisel3.util._
import acc_proc_shared._


class AccProc {
      var pc = Wire(RegInit(0.U(16.W)))  // Signal PC uint16
      var acc = Wire(RegInit(0.U(16.W))) // Signal ACC uint16
      var state = Wire(RegInit(State.fetch))
      val memoryRegister = SyncReadMem(256, UInt(16.W)) // Initialise les 256 registre possible initalises a valeur 0
      var ir = new Instruction(0.U)
      
      switch(state){
            is(State.fetch){
                  state := RegNext(State.decode) // Assigns value on next risingEdge
                  ir = new Instruction(RegNext(Program.programList(pc)))
            }
            is(State.decode){
                  state := RegNext(State.execute)
                  switch(ir.io.operation){
                        is(Operation.add){
                              acc := RegNext(acc + memoryRegister(ir.io.addr))
                        }
                        is(Operation.mul){
                              acc := RegNext(acc * memoryRegister(ir.io.addr))
                        }
                        is(Operation.ld){
                              acc := RegNext(memoryRegister(ir.io.addr))
                        }
                        is(Operation.st){ // Line 31 does not necessarily work. 
                              memoryRegister(ir.io.addr).apply(acc) 
                        }
                  }
            }
            is(State.execute){
                  state := RegNext(State.fetch)
                  pc := RegNext(pc + 1.U)
            }
      }
}