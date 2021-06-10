package emulator;

public class Registers {
    public final byte[] registers;
    private short instructionRegister;
    private short stackPointer;
    private short programCounter;

    public Registers() {
        this.registers = new byte[0x10];
        this.instructionRegister = 0;
        this.programCounter = 0x200;
        this.stackPointer = 0xF;
        this.instructionRegister = 0;
    }

    public void incrementProgramCounter(){
        programCounter += 2;
    }

    public void setInstructionRegister(short instructionRegister) {
        this.instructionRegister = instructionRegister;
    }
    public void addInstructionRegister(short add) {
        this.instructionRegister += add;
    }

    public void setProgramCounter(short programCounter) {
        this.programCounter = programCounter;
    }

    public short getProgramCounter() {
        return programCounter;
    }

    public short getInstructionRegister() {
        return instructionRegister;
    }

    public short getStackPointer(){
        return stackPointer;
    }

    public void incrementStackPointer(){
        stackPointer++;
    }

    public void decrementStackPointer(){
        stackPointer--;
    }
}
