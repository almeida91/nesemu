package nesemu.integ.cpu;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import nesemu.cpu.InstructionCall;
import nesemu.debug.Debugger;
import nesemu.integ.common.TestMemory;
import nesemu.memory.Memory;
import nesemu.memory.Stack;

public class CpuTestsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Memory.class).to(TestMemory.class).asEagerSingleton();
        bind(Stack.class).asEagerSingleton();
    }

    @Provides
    public Debugger provideDebugger() {
        return new Debugger(){
            @Override
            public void callInstruction(InstructionCall instructionCall, int address) {

            }
        } ;
    }
}
