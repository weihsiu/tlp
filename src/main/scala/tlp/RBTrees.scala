package tlp

import scala.compiletime.ops.int._
import scala.Ordering.Implicits._

/**
 * https://en.wikipedia.org/wiki/Red%E2%80%93black_tree
 * https://fstaals.net/RedBlackTree.html
 * 
 * 1. Each node is either red or black.
 * 2. The root is black.
 * 3. All leaves are black.
 * 4. If a node is red, then both its children are black.
 * 5. Every path from a given node to any of its descendant leaf nodes goes through the same number of black nodes.
 */
 object RBTrees:
  enum Color:
    // 1
    case Red, Black
  import Color._

  enum Node[C <: Color, N <: Int, A]:
    // 3
    case Leaf() extends Node[Black.type, 0, A]
    // 5
    case BNode(left: Node[_, N, A], x: A, right: Node[_, N, A]) extends Node[Black.type, N + 1, A]
    // 4
    case RNode(left: Node[Black.type, N, A], x: A, right: Node[Black.type, N, A]) extends Node[Red.type, N, A]
  import Node._

  // 2
  case class RBTree[A](root: Node[Black.type, _, A])

  object RBTree:
    def empty[A]: RBTree[A] = RBTree[A](Leaf())
    def insert[A : Ordering](x: A, tree: RBTree[A]): RBTree[A] =
      enum BlackInsert[N <: Int, A]:
        case BDone(node: Node[Black.type, N, A]) extends BlackInsert[N, A]
        case NewRed(node: Node[Red.type, N, A]) extends BlackInsert[N, A]
      import BlackInsert._
      enum RedInsert[N <: Int, A]:
        case RDone(node: Node[Red.type, N, A]) extends RedInsert[N, A]
        case RRed(redRed: RedRed[N, A]) extends RedInsert[N, A]
      import RedInsert._
      enum RedRed[N <: Int, A]:
        case LeftR(left: Node[Red.type, N, A], x: A, right: Node[Black.type, N, A]) extends RedRed[N, A]
        case RightR(left: Node[Black.type, N, A], x: A, right: Node[Red.type, N, A]) extends RedRed[N, A]
      import RedRed._
      enum InsertN[N <: Int, A]:
        case RIns(insert: RedInsert[N, A]) extends InsertN[N, A]
        case BIns(insert: BlackInsert[N, A]) extends InsertN[N, A]
      import InsertN._
      def insertN[C <: Color, N <: Int, A : Ordering](x: A, node: Node[C, N, A]): InsertN[N, A] =
        node match
          case Leaf() => BIns(insertL(x, node))
          case BNode(_, _, _) => BIns(insertB(x, node))
          case RNode(_, _, _) => RIns(insertR(x, node))
      def insertL[A](x: A, leaf: Node[Black.type, 0, A]): BlackInsert[0, A] =
        NewRed(RNode(Leaf(), x, Leaf()))
      def insertR[N <: Int, A : Ordering](x: A, node: Node[Red.type, N, A]): RedInsert[N, A] =
        node match
          case RNode(l, y, r) if x <= y => insertB(x, l) match
            case BDone(l) => RDone(RNode(l, y, r))
            case NewRed(l) => RRed(LeftR(l, y, r))
          case RNode(l, y, r) => insertB(x, r) match
            case BDone(r) => RDone(RNode(l, y, r))
            case NewRed(r) => RRed(RightR(l, y, r))
      def insertB[N <: Int, A : Ordering](x: A, node: Node[Black.type, N, A]): BlackInsert[N, A] =
        node match
          case Leaf() => insertL(x, node)
          case BNode(l, y, r) if x <= y => insertN(x, l) match
            case BIns(BDone(l)) => BDone(BNode(l, y, r))
            case BIns(NewRed(l)) => BDone(BNode(l, y, r))
            case RIns(RDone(l)) => BDone(BNode(l, y, r))
            case RIns(RRed(l)) => NewRed(balanceL(l, y, r))
          case BNode(l, y, r) => insertN(x, r) match
            case BIns(BDone(r)) => BDone(BNode(l, y, r))
            case BIns(NewRed(r)) => BDone(BNode(l, y, r))
            case RIns(RDone(r)) => BDone(BNode(l, y, r))
            case RIns(RRed(r)) => NewRed(balanceR(l, y, r))
      def balanceL[C <: Color, N <: Int, A](left: RedRed[N, A], x: A, right: Node[C, N, A]): Node[Red.type, N + 1, A] =
        left match
          case LeftR(RNode(lll, llx, llr), lx, lr) => RNode(BNode(lll, llx, llr), lx, BNode(lr, x, right))
          case RightR(ll, lx, RNode(lrl, lrx, lrr)) => RNode(BNode(ll, lx, lrl), lrx, BNode(lrr,  x, right))
      def balanceR[C <: Color, N <: Int, A](left: Node[C, N, A], x: A, right: RedRed[N, A]): Node[Red.type, N + 1, A] =
        right match
          case LeftR(RNode(rll, rlx, rlr), rx, rr) => RNode(BNode(left, x, rll), rlx, BNode(rlr, rx, rr))
          case RightR(rl, rx, RNode(rrl, rrx, rrr)) => RNode(BNode(left, x, rl), rx, BNode(rrl, rrx, rrr))
      def turnBlack[N <: Int, A](node: Node[Red.type, N, A]): Node[Black.type, N + 1, A] =
        node match
          case RNode(l, x, r) => BNode(l, x, r)
      tree match
        case RBTree(n) => insertB(x, n) match
          case BDone(bn) => RBTree(bn)
          case NewRed(rn) => RBTree(turnBlack(rn))

  @main
  def runRBTrees(): Unit =
    import RBTree._
    val t = (1 to 10).foldRight(empty[Int])(insert)
    println(t)
