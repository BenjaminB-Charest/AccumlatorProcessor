package acc_proc

import chisel3._
import chisel3.util._
import acc_proc_shared._


class AccProc extends Module{
      
      val io = new Bundle {
        var addr = Input(UInt(8.W)) // 256 Addresses inside memory
        var operation = Input(UInt(3.W)) // 3.W since we have 6 options --> 3 bits can fit this.
      }

      var pc = Wire(RegInit(0.U(16.W)))  // Signal PC uint16
      var acc = Wire(RegInit(0.U(16.W))) // Signal ACC uint16
      var state = Wire(RegInit(State.fetch))
      val memoryRegister = SyncReadMem(256, UInt(16.W)) // Initialise les 256 registre possible initalises a valeur 0
      
      var ir = RegInit(0.U(Program.programList(pc)))
      io.addr := retrieveAddress(ir)
      io.operation := retrieveOperation(ir, io.addr)

      def retrieveAddress (ir: UInt) = {
            ir % 256.U
      }

      def retrieveOperation (ir: UInt, address: UInt) = {
            (ir - address) / 256.U
      }

      switch(state){
            is(State.fetch){
                  state := RegNext(State.decode) // Assigns value on next risingEdge
                  ir := (RegNext(Program.programList(pc)))
            }
            is(State.decode){
                  state := RegNext(State.execute)
                  switch(io.operation){
                        is(Operation.add){
                              acc := RegNext(acc + memoryRegister(io.addr))
                        }
                        is(Operation.mul){
                              acc := RegNext(acc * memoryRegister(io.addr))
                        }
                        is(Operation.ld){
                              acc := RegNext(memoryRegister(io.addr))
                        }
                        is(Operation.st){ // Line 31 does not necessarily work. 
                              memoryRegister(io.addr).apply(acc) 
                        }
                  }
            }
            is(State.execute){
                  state := RegNext(State.fetch)
                  pc := RegNext(pc + 1.U)
            }
      }
}
