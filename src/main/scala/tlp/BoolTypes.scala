package tlp

import scala.util.Not

object BoolTypes:

  sealed trait BoolType:
    type Not <: BoolType
    type Or[That <: BoolType] <: BoolType

  sealed trait TrueType extends BoolType:
    type Not = FalseType
    type Or[That <: BoolType] = TrueType

  sealed trait FalseType extends BoolType:
    type Not = TrueType
    type Or[That <: BoolType] = That

  summon[TrueType =:= TrueType]
  summon[FalseType =:= FalseType]
  summon[TrueType#Not =:= FalseType]
  summon[FalseType#Not =:= TrueType]
  summon[TrueType#Or[TrueType] =:= TrueType]
  summon[TrueType#Or[FalseType] =:= TrueType]
  summon[FalseType#Or[TrueType] =:= TrueType]
  summon[FalseType#Or[FalseType] =:= FalseType]
  summon[Not[TrueType =:= FalseType]]
  summon[Not[FalseType =:= TrueType]]
  

