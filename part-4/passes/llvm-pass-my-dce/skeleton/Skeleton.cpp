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

struct SimpleDCE : public FunctionPass {
   std::map<std::string, int> opCounter;
   std::map<Instruction*, int> instructions;

   class usedef {
   public:
    std::set<Instruction*> use;
    std::set<Instruction*> def;
   };
   class live {
   public:
    std::set<Instruction*> in;
    std::set<Instruction*> out;
  };
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
        SmallVector<BasicBlock*, 64> WL_bb;
// std::map<BasicBlock*, std::set<Instruction*>*> liveout_bb;
      //  std::map<BasicBlock*, std::set<Instruction*>*> livein_bb;
       // std::set<BasicBlock*> WL_bb; 
        std::map<BasicBlock*, usedef> useMap;
        std::map<BasicBlock*, live> liveMap;
        static int id = 1;
        for (inst_iterator i = inst_begin(F), E = inst_end(F); i != E; ++i, ++id){
           instructions.insert(std::make_pair(&*i, id));
        }
        //SmallPtrSet<Instruction*, 64> ALV;
        bool changed = false;
        for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb) {
        usedef a;
        for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i) {
          int num_op = i->getNumOperands();
          for (int j = 0; j < num_op; j++) {
            Value *v = i->getOperand(j); //get the use set of the basic block
            if (isa<Instruction>(v)) {
              Instruction *inst = (Instruction*) v;
              if (!a.def.count(inst)){ //the instruction isn't already defined
                a.use.insert(inst);}
            }
         }
          a.def.insert(&*i);  
        }
        useMap.insert(std::make_pair(&*bb, a));
       }
      for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb) {
                  WL_bb.push_back(&*bb);
      }
      while (!WL_bb.empty()) {
        BasicBlock *cur = WL_bb.pop_back_val();
        live bbl = liveMap.find(cur)->second;
      //  bool islive = !liveMap.count(b);
        std::set<Instruction*> newOut;
        for (succ_iterator sc = succ_begin(cur), e = succ_end(cur); sc != e; ++sc) {
          std::set<Instruction*> l_in(liveMap.find(*sc)->second.in);
          for (auto s : l_in){
           newOut.insert(s);  
          }
        }

        if (newOut != liveMap.find(cur)->second.out){
          liveMap.find(cur)->second.in.clear();
          liveMap.find(cur)->second.out = newOut;
          for (auto i : newOut){
           liveMap.find(cur)->second.in.insert(i);
          }
          for (auto i : useMap.find(cur)->second.def) {
          liveMap.find(cur)->second.in.erase(i);
          }
          for (auto i : useMap.find(cur)->second.use) {
           liveMap.find(cur)->second.in.insert(i);
          }
         for (pred_iterator p = pred_begin(cur), e = pred_end(cur); p != e; ++p)
            WL_bb.push_back(*p);
         }
      }
      for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb) {
      }
/*             for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i) {
                  if(isInstDead(&*i)){
         //           ALV.insert(&*i);
                    WL.push_back(&*i);
                  }
            }
            while (!WL.empty()) {
               Instruction* i = WL.pop_back_val();
               i->eraseFromParent();
            }        */

   /*           for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i) {
                if (isInstructionTriviallyDead(i)) {
                    i->eraseFromParent();
                    isDead = true;
                    ++NumIE;
                }
                }
            }  */
	
      
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
