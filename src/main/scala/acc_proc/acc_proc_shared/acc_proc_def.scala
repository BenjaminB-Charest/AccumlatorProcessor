package acc_proc_shared

import chisel3._
import chisel3.util._

object Operation {
    val add :: mul :: st :: ld :: stop :: nop :: Nil = Enum(6)
}

object State {
    val fetch :: decode :: execute :: Nil = Enum(3)
}
