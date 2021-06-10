package emulator;

import javafx.scene.layout.Pane;

import java.io.File;
import java.util.Random;

public class Emulator {
    private final Registers registers;
    private final Memory memory;
    private final Instructions instructions;

    Emulator(File romFile, Pane gamePane) throws Exception {
        this.registers = new Registers();
        this.memory = new Memory(romFile);

        Random randomGenerator = new Random();
        Display display = new Display(gamePane);
        Keyboard keyboard = new Keyboard(gamePane);
        Timers timers = new Timers();

        instructions = new Instructions(registers, memory, display, randomGenerator, keyboard, timers);
    }

    public void step() {
        short instruction = memory.getRAMShort(registers.getProgramCounter());
        Instruction instructionObject = new Instruction(instruction);

        try{
            decodeAndExecuteInstruction(instructionObject);
        } catch (InstructionNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void decodeAndExecuteInstruction(Instruction instruction) throws InstructionNotFoundException {
        switch (instruction.NibbleA) {
            case 0x0 -> decodeAndExecuteInstruction0x0(instruction);
            case 0x1 -> instructions.JP(instruction.nnn);
            case 0x2 -> instructions.CALL(instruction.nnn);
            case 0x3 -> instructions.SE_kk(instruction.NibbleB, instruction.ByteB);
            case 0x4 -> instructions.SNE_kk(instruction.NibbleB, instruction.ByteB);
            case 0x5 -> instructions.SE(instruction.NibbleB, instruction.NibbleC);
            case 0x6 -> instructions.LD_kk(instruction.NibbleB, instruction.ByteB);
            case 0x7 -> instructions.SUM_kk(instruction.NibbleB, instruction.ByteB);
            case 0x8 -> decodeAndExecuteInstruction0x8(instruction);
            case 0x9 -> instructions.SNE(instruction.NibbleB, instruction.NibbleC);
            case 0xA -> instructions.LD_nnn(instruction.nnn);
            case 0xB -> instructions.JP_v0(instruction.nnn);
            case 0xC -> instructions.RND(instruction.NibbleB, instruction.ByteB);
            case 0xD -> instructions.DRW(registers.registers[instruction.NibbleB], registers.registers[instruction.NibbleC], instruction.NibbleD);
            case 0xE -> decodeAndExecuteInstruction0xE(instruction);
            case 0xF -> decodeAndExecuteInstruction0xF(instruction);
            default -> throw new InstructionNotFoundException(instruction);
        }
    }

    private void decodeAndExecuteInstruction0x0(Instruction instruction) {
        switch (instruction.ByteB) {
            case 0x0 -> instructions.SYS();
            case (byte) 0xE0 -> instructions.CLS();
            case (byte) 0xEE -> instructions.RET();
        }
    }

    private void decodeAndExecuteInstruction0x8(Instruction instruction) throws InstructionNotFoundException {
        switch (instruction.NibbleD) {
            case 0x0 -> instructions.LD(instruction.NibbleB, instruction.NibbleC);
            case 0x1 -> instructions.OR(instruction.NibbleB, instruction.NibbleC);
            case 0x2 -> instructions.AND(instruction.NibbleB, instruction.NibbleC);
            case 0x3 -> instructions.XOR(instruction.NibbleB, instruction.NibbleC);
            case 0x4 -> instructions.SUM(instruction.NibbleB, instruction.NibbleC);
            case 0x5 -> instructions.SUB(instruction.NibbleB, instruction.NibbleC);
            case 0x6 -> instructions.SHR(instruction.NibbleB);
            case 0x7 -> instructions.SUBN(instruction.NibbleB, instruction.NibbleC);
            case 0xE -> instructions.SHL(instruction.NibbleB);
            default -> throw new InstructionNotFoundException(instruction);
        }
    }

    private void decodeAndExecuteInstruction0xE(Instruction instruction) throws InstructionNotFoundException {
        switch (instruction.ByteB) {
            case (byte) 0x9E -> instructions.SKP(instruction.NibbleB);
            case (byte) 0xA1 -> instructions.SKNP(instruction.NibbleB);
            default -> throw new InstructionNotFoundException(instruction);
        }
    }

    private void decodeAndExecuteInstruction0xF(Instruction instruction) throws InstructionNotFoundException {
        switch (instruction.ByteB) {
            case 0x07 -> instructions.LD_DT(instruction.NibbleB);
            case 0x0A -> instructions.LD_KEY(instruction.NibbleB);
            case 0x15 -> instructions.LD_SET_DT(instruction.NibbleB);
            case 0x18 -> instructions.SYS();
            case 0x1E -> instructions.ADD_I(instruction.NibbleB);
            case 0x29 -> instructions.LD_FIND_DIGIT(instruction.NibbleB);
            case 0x33 -> instructions.LD_BCD(instruction.NibbleB);
            case 0x55 -> instructions.LD_STORE_REGISTERS(instruction.NibbleB);
            case 0x65 -> instructions.LD_READ_REGISTERS(instruction.NibbleB);
            default -> throw new InstructionNotFoundException(instruction);
        }
    }

}
