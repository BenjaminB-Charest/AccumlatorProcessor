package acc_proc_shared

import chisel3._
import acc_proc_shared._

object Program{
        val programList = Vec(9,UInt(16.W))
        programList(0):= 1795.U
        programList(1):= 2561.U
        programList(2):= 2048.U
        programList(3):= 2561.U
        programList(4):= 2304.U
        programList(5):= 2818.U
        programList(6):= 4.U,
}