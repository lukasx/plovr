/*
 * Copyright 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.template.soy.exprtree;

import com.google.template.soy.base.SourceLocation;
import com.google.template.soy.types.SoyType;
import com.google.template.soy.types.primitive.IntType;

import java.util.Objects;

/**
 * Node representing an integer value.
 *
 * <p> Important: Do not use outside of Soy code (treat as superpackage-private).
 *
 */
public final class IntegerNode extends AbstractPrimitiveNode {

  /** The integer value. */
  private final int value;

  /**
   * @param value The integer value.
   * @param sourceLocation The node's source location.
   */
  public IntegerNode(int value, SourceLocation sourceLocation) {
    super(sourceLocation);
    this.value = value;
  }


  /**
   * Copy constructor.
   * @param orig The node to copy.
   */
  private IntegerNode(IntegerNode orig) {
    super(orig);
    this.value = orig.value;
  }


  @Override public Kind getKind() {
    return Kind.INTEGER_NODE;
  }


  @Override public SoyType getType() {
    return IntType.getInstance();
  }


  /** Returns the integer value. */
  public int getValue() {
    return value;
  }


  @Override public String toSourceString() {
    return Integer.toString(value);
  }


  @Override public IntegerNode clone() {
    return new IntegerNode(this);
  }


  @Override public boolean equals(Object other) {
    if (other == null || other.getClass() != this.getClass()) { return false; }
    IntegerNode otherInt = (IntegerNode) other;
    return value == otherInt.value;
  }


  @Override public int hashCode() {
    return Objects.hash(this.getClass(), value);
  }
}
