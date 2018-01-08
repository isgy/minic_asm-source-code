/*
 * =====================================================================================
 *
 *       Filename:  Skeleton.cpp
 *
 *    Description:
 *
 *        Version:  1.0
 *        Created:  31/12/17 19:27:38
 *       Revision:  none
 *       Compiler:  gcc
 *
 *         Author:  YOUR NAME (),
 *   Organization:
 *
 * =====================================================================================
 */
#define DEBUG_TYPE "MyDCE"
#include "llvm/Pass.h"
#include "llvm/IR/Function.h"
#include "llvm/Support/raw_ostream.h"
#include "llvm/IR/LegacyPassManager.h"
#include "llvm/Transforms/IPO/PassManagerBuilder.h"
#include "llvm/IR/Instructions.h"

#include "llvm/ADT/SmallVector.h"

#include <vector>
using namespace llvm;
//using namespace std;

//STATISTIC(NumIE, "no. of instructions removed 1");
//STATISTIC(NumIE2, "no. of inst removed");
namespace {
struct MyDCE : public FunctionPass {
   std::map<std::string, int> opCounter;
    static char ID;
    int NumIE = 0;
    int NumIE2 = 0;
    MyDCE() : FunctionPass(ID) {}
    virtual bool runOnFunction(Function &F) {
        SmallVector<Instruction*, 64> WL;
    //    std::set<Instruction*> dc;
		  errs() << "I saw a function called " << F.getName() << "!\n";
        errs() << "Function " << F.getName() << '\n';
        for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb) {
            bool isDead = false;
            for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i) {
                  if (!(isa<TerminatorInst>(*i)) && !i->mayHaveSideEffects()){
                     WL.push_back(&*i);
						}
                 	if(opCounter.find(i->getOpcodeName()) == opCounter.end()) {
                 	   opCounter[i->getOpcodeName()] = 1;
                  } else {
                	   opCounter[i->getOpcodeName()] += 1;
                	}
            }
            while (!WL.empty()) {
               Instruction* i = WL.pop_back_val();
               i->eraseFromParent();
               ++NumIE;
               ++NumIE2;
            }

          /*  for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i) {
                if (isInstructionTriviallyDead(i)) {
                    i->eraseFromParent();
                    isDead = true;
                    ++NumIE;
                }
                if(opCounter.find(i->getOpcodeName()) == opCounter.end()) {
                    opCounter[i->getOpcodeName()] = 1;
                } else {
                    opCounter[i->getOpcodeName()] += 1;
                }
            } */
	
        }
        std::map <std::string, int>::iterator i = opCounter.begin();
        std::map <std::string, int>::iterator e = opCounter.end();
        while (i != e) {
            errs() << i->first << ": " << i->second << "\n";
            i++;
        }
        errs() << "\n";
        opCounter.clear();
        //return !WL.empty();
        return false;
    }
};
}
char MyDCE::ID = 0;
__attribute__((unused)) static RegisterPass<MyDCE>
X("skeletonpass", "liveness analysis pass"); // NOLINT
static void registerMyDCEPass(const PassManagerBuilder &,
                         legacy::PassManagerBase &PM) {
  PM.add(new MyDCE());
}
static RegisterStandardPasses
  RegisterMyPass(PassManagerBuilder::EP_EarlyAsPossible,
                 registerMyDCEPass);
