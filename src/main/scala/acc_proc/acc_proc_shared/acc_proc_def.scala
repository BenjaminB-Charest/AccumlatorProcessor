package acc_proc_shared

import chisel3._
import chisel3.util._

class Instruction(value : UInt) extends Module {
    val io = new Bundle {
        var addr = UInt(8.W) // 256 Addresses inside memory
        var operation = UInt(3.W) // 3.W since we have 6 options --> 3 bits can fit this.
    }
    io.addr = value % 256.U
    io.operation = (value - io.addr) / 256.U
}

object Operation {
    val add :: mul :: st :: ld :: stop :: nop :: Nil = Enum(6)
}

object State {
    val fetch :: decode :: execute :: Nil = Enum(3)
}
