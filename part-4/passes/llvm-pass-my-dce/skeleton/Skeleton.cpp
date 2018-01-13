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

#include "llvm/ADT/BitVector.h"
#include "llvm/ADT/PostOrderIterator.h"
#include "llvm/IR/InstIterator.h"

#include "llvm/ADT/Statistic.h"
#include <vector>

using namespace llvm;
//using namespace std;
STATISTIC(NumIE, "no. of insts removed");

namespace {

struct BBLiveness {
	BitVector *use;
	BitVector *def;
	BitVector *in;
	BitVector *out;
	BasicBlock *bb;
};
struct SimpleDCE : public FunctionPass {
   std::map<std::string, int> opCounter;
   std::map<const Instruction*, int> instructions;
    static char ID;
    int NumIE = 0;
    int NumIE2 = 0;
    SimpleDCE() : FunctionPass(ID) {}
    virtual bool deadInstr(Instruction* instr) {
      if (isa<TerminatorInst>(instr) || instr->mayHaveSideEffects() || isa<LandingPadInst>(instr)) {
          return false;
      }
      return true;
    }
    virtual bool runOnFunction(Function &F) {
        SmallVector<Instruction*, 64> WL;
        std::map<BasicBlock*, std::set<Instruction*>*> liveout_bb;
        std::map<BasicBlock*, std::set<Instruction*>*> livein_bb;
        std::set<BasicBlock*> WL_bb; 
        static int id = 1;
        for (inst_iterator i = inst_begin(F), E = inst_end(F); i != E; ++i, ++id){
           instructions.insert(std::make_pair(&*i, id));
        }
        //SmallPtrSet<Instruction*, 64> ALV;
        bool changed = false;
      
      DenseMap<const BasicBlock*, usedef> bbMap;
      
      findUseDef(F, bbMap);

      DenseMap<const BasicBlock*, in_out> liveMap;
      in_out_sets(F, bbMap, liveMap);

      DenseMap<const Instruction*, in_out> instliveMap;
      in_out_sets(F, bbMap, instliveMap);

      for (inst_iterator i = inst_begin(F), E = inst_end(F); i != E; ++i) {
        beforeAfter s = iBAMap.lookup(&*i);
        errs() << "%" << instMap.lookup(&*i) << ": { ";
        std::for_each(s.before.begin(), s.before.end(), print_elem);
        errs() << "} { ";
        std::for_each(s.after.begin(), s.after.end(), print_elem);
        errs() << "}\n";
      }

    //    std::set<Instruction*> dc;
		  errs() << "I saw a function called " << F.getName() << "!\n";
        errs() << "Function " << F.getName() << '\n';
        for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb) {
                  bool isDead = false;
                  WL_bb.insert(&*bb);
                  std::set<Instruction*>* livein_i = new std::set<Instruction*>();
                  livein_bb.insert(std::pair<BasicBlock*, std::set<Instruction*>*>(&*bb, livein_i));
                  std::set<Instruction*>* liveout_i = new std::set<Instruction*>();
                  liveout_bb.insert(std::pair<BasicBlock*, std::set<Instruction*>*>(&*bb, liveout_i)); 
                  outs() << "Basic blocks of " << F.getName() << " in post-order:\n";
                  for (po_iterator<BasicBlock *> I = po_begin(&F.getEntryBlock()),
                               IE = po_end(&F.getEntryBlock());
                               I != IE; ++I) {
  outs() << "  " << (*I)->getName() << "\n";
}
            for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i) {
                  if(isInstDead(&*i)){
         //           ALV.insert(&*i);
                    WL.push_back(&*i);
                  }
            }
            while (!WL.empty()) {
               Instruction* i = WL.pop_back_val();
               i->eraseFromParent();
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
