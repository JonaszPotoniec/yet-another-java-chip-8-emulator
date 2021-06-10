package emulator;

import java.util.Random;

public class Instructions {
    private final Registers registers;
    private final Memory memory;
    private final Display display;
    private final Random randomGenerator;
    private final Keyboard keyboard;
    private final Timers timers;

    Instructions(Registers registers, Memory memory, Display display, Random randomGenerator, Keyboard keyboard, Timers timers){
        this.display = display;
        this.randomGenerator = randomGenerator;
        this.keyboard = keyboard;
        this.timers = timers;
        this.registers = registers;
        this.memory = memory;
    }

    //0nnn
    public void SYS() {
        registers.incrementProgramCounter();
    }

    //00E0
    public void CLS() {
        display.clearScreen();
        registers.incrementProgramCounter();
    }

    //00EE
    public void RET() {
        registers.setProgramCounter(memory.getStack(registers.getStackPointer()));
        registers.incrementStackPointer();
        registers.incrementProgramCounter();
    }

    //1nnn
    public void JP(int addr) {
        registers.setProgramCounter((short) addr);
    }

    //2nnn
    public void CALL(short nnn) {
        registers.decrementStackPointer();
        memory.setStack(registers.getStackPointer(), registers.getProgramCounter());
        registers.setProgramCounter(nnn);
    }

    //3xkk
    public void SE_kk(byte a, byte kk) {
        if (registers.registers[a] == kk) registers.incrementProgramCounter();
        registers.incrementProgramCounter();
    }

    //4xy0
    public void SNE_kk(byte a, byte kk) {
        if (registers.registers[a] != kk) registers.incrementProgramCounter();
        registers.incrementProgramCounter();
    }

    //5xy0
    public void SE(byte a, byte b) {
        if (registers.registers[a] == registers.registers[b]) registers.incrementProgramCounter();
        registers.incrementProgramCounter();
    }

    //6xkk
    public void LD_kk(byte a, byte kk) {
        registers.registers[a] = kk;
        registers.incrementProgramCounter();
    }

    //7xkk
    public void SUM_kk(byte a, byte kk) {
        registers.registers[a] += kk;
        registers.incrementProgramCounter();
    }

    //8xy0
    public void LD(byte a, byte b) {
        registers.registers[a] = registers.registers[b];
        registers.incrementProgramCounter();
    }

    //8xy1
    public void OR(byte a, byte b) {
        registers.registers[a] |= registers.registers[b];
        registers.incrementProgramCounter();
    }

    //8xy2
    public void AND(byte a, byte b) {
        registers.registers[a] &= registers.registers[b];
        registers.incrementProgramCounter();
    }

    //8xy3
    public void XOR(byte a, byte b) {
        registers.registers[a] ^= registers.registers[b];
        registers.incrementProgramCounter();
    }

    //8xy4
    public void SUM(byte a, byte b) {
        short result = (short) ((registers.registers[a] & 0xFF) + (registers.registers[b] & 0xFF));
        registers.registers[a] = (byte) (result & 0x00FF);
        registers.registers[0xF] = (byte) (result > 0xFF ? 1 : 0);
        registers.incrementProgramCounter();
    }

    //8xy5
    public void SUB(byte a, byte b) {
        registers.registers[0xF] = (byte) ((registers.registers[a] & 0xFF) > (registers.registers[b] & 0xFF) ? 1 : 0);
        registers.registers[a] -= registers.registers[b];
        registers.incrementProgramCounter();
    }

    //8xy6
    public void SHR(byte a) {
        registers.registers[0xF] = (byte) (registers.registers[a] & 1);
        registers.registers[a] = (byte) ((registers.registers[a]&0xFF) >>> 1);
        registers.incrementProgramCounter();
    }

    //8xy7
    public void SUBN(byte a, byte b) {
        registers.registers[0xF] = (byte) (registers.registers[a] < registers.registers[b] ? 1 : 0);
        registers.registers[a] = (byte) (registers.registers[b] - registers.registers[a]);
        registers.incrementProgramCounter();
    }

    //8xyE
    public void SHL(byte a) {
        registers.registers[0xF] = (byte) ((registers.registers[a] & 0b10000000) > 0 ? 1 : 0);
        registers.registers[a] <<= 1;
        registers.incrementProgramCounter();
    }

    //9xy0
    public void SNE(byte a, byte b) {
        if (registers.registers[a] != registers.registers[b]) registers.incrementProgramCounter();
        registers.incrementProgramCounter();
    }

    //Annn
    public void LD_nnn(short nnn) {
        registers.setInstructionRegister(nnn);
        registers.incrementProgramCounter();
    }

    //Bnnn
    public void JP_v0(short nnn) {
        registers.setProgramCounter((short) ((registers.registers[0] & 0xff) + (nnn & 0xfff)));
    }

    //Cxkk
    public void RND(byte a, byte kk) {
        registers.registers[a] = (byte) (randomGenerator.nextInt() & kk);
        registers.incrementProgramCounter();
    }

    //Dxyn
    public void DRW(byte x, byte y, byte n) {
        byte[] tab = new byte[n];
        System.arraycopy(memory.getAllRAM(), registers.getInstructionRegister(), tab, 0, n);
        registers.registers[0xF] = (byte) (display.drawSprite(tab, x & 0xFF, y & 0xFF) ? 1 : 0);
        registers.incrementProgramCounter();
    }

    //Ex9E
    public void SKP(byte a){
        if(keyboard.isKeyPressed(registers.registers[a])) registers.incrementProgramCounter();
        registers.incrementProgramCounter();
    }
    //ExA1
    public void SKNP(byte a){
        if(!keyboard.isKeyPressed(registers.registers[a])) registers.incrementProgramCounter();
        registers.incrementProgramCounter();
    }
    //Fx07
    public void LD_DT(byte a){
        registers.registers[a] = (byte) timers.getDelayTimer();
        registers.incrementProgramCounter();
    }
    //Fx0A
    public void LD_KEY(byte a){
        for(int i = 0; i < 16; i++) {
            if (keyboard.isKeyPressed(i)) {
                registers.registers[a] = (byte) i;
                registers.incrementProgramCounter();
                return;
            }
        }
    }
    //Fx15
    public void LD_SET_DT(byte a){
        timers.setDelayTimer(a & 0xFF);
        registers.incrementProgramCounter();
    }

    //Fx1E
    public void ADD_I(byte a) {
        registers.addInstructionRegister((short) (registers.registers[a] & 0xFF));
        registers.incrementProgramCounter();
    }

    //Fx29
    public void LD_FIND_DIGIT(byte a) {
        registers.setInstructionRegister((short) ((registers.registers[a] & 0xFF) * 5));
        registers.incrementProgramCounter();
    }

    //Fx33
    public void LD_BCD(byte a) {
        int temp = registers.registers[a] & 0xFF;
        for (int i = 2; i >= 0; i--) {
            memory.setRAM(registers.getInstructionRegister() + i, (byte) (temp % 10));
            temp /= 10;
        }
        registers.incrementProgramCounter();
    }

    //Fx55
    public void LD_STORE_REGISTERS(byte a) {
        for (int i = 0; i <= (a & 0xFF); i++) {
            memory.setRAM(registers.getInstructionRegister() + i, registers.registers[i]);
        }
        registers.incrementProgramCounter();
    }

    //Fx65
    public void LD_READ_REGISTERS(byte a) {
        for (int i = 0; i <= (a & 0xFF); i++) {
            registers.registers[i] = memory.getRAM(registers.getInstructionRegister() + i);
        }
        registers.incrementProgramCounter();
    }
}
