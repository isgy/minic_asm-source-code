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
#define DEBUG_TYPE "SimpleDCE"
#include "llvm/Pass.h"
#include "llvm/IR/Function.h"
#include "llvm/Support/raw_ostream.h"
#include "llvm/IR/LegacyPassManager.h"
#include "llvm/Transforms/IPO/PassManagerBuilder.h"
#include "llvm/IR/Instructions.h"
#include "llvm/Transforms/Utils/Local.h"
#include "llvm/ADT/SmallVector.h"

#include <vector>
using namespace llvm;
//using namespace std;

namespace {
struct SimpleDCE : public FunctionPass {
   std::map<std::string, int> opCounter;
    static char ID;
    int NumIE = 0;
    int NumIE2 = 0;
    SimpleDCE() : FunctionPass(ID) {}
    virtual bool runOnFunction(Function &F) {
        SmallVector<Instruction*, 64> WL;
        //SmallPtrSet<Instruction*, 64> ALV;

    //    std::set<Instruction*> dc;
		  errs() << "I saw a function called " << F.getName() << "!\n";
        errs() << "Function " << F.getName() << '\n';
        for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb) {
            bool isDead = false;
            for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i) {
//                  if(!(isa<TerminatorInst>(*i)) && !i->mayHaveSideEffects()){
                  if(isInstructionTriviallyDead(&*i)){
         //           ALV.insert(&*i);
                    WL.push_back(&*i);
                  }
            }
            while (!WL.empty()) {
               Instruction* i = WL.pop_back_val();
               i->eraseFromParent();
           //    for(Use &O : i->operands()){
            //      if(Instruction *i = dyn_cast<Instruction>(O)){
          //           if(ALV.insert(i).second){
             //           WL.push_back(i);
              //       }
               //   }
               //}
            }

   /*           for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i) {
                if (isInstructionTriviallyDead(i)) {
                    i->eraseFromParent();
                    isDead = true;
                    ++NumIE;
                }
                }
            }  */
	
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
char SimpleDCE::ID = 0;
__attribute__((unused)) static RegisterPass<SimpleDCE>
X("skeletonpass", "Simple dead code elimination"); // NOLINT
static void registerSimpleDCEPass(const PassManagerBuilder &,
                         legacy::PassManagerBase &PM) {
  PM.add(new SimpleDCE());
}
static RegisterStandardPasses
  RegisterMyPass(PassManagerBuilder::EP_EarlyAsPossible,
                 registerSimpleDCEPass);
