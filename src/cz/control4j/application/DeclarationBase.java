/*
 *  Copyright 2013, 2014, 2016 Jiri Lidinsky
 *
 *  This file is part of control4j.
 *
 *  control4j is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, version 3.
 *
 *  control4j is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with control4j.  If not, see <http://www.gnu.org/licenses/>.
 */

package cz.control4j.application;

import cz.lidinsky.tools.text.DeclarationReference;

/**
 *
 *  A common base for classes that hold data about the place where
 *  they were declared or from where they come from.
 *
 *  <p>This class provides common means for keeping information
 *  about a place whre the object come from or where was declared.
 *  This is useful for reporting potential problems.
 *
 *  @see cz.lidinsky.tools.text.DeclarationReference
 *
 */
public abstract class DeclarationBase extends ObjectBase
{

  /**
   *  Contains information about a place where the object was
   *  declared in human readable form. May not contain null value!
   */
  private DeclarationReference declarationReference;

  /**
   *
   */
  public DeclarationBase()
  {
    declarationReference = DeclarationReference.get(this.getClass().getName());
  }

  protected DeclarationReference getThisObjectIdentification()
  {
    return DeclarationReference.get(this.getClass().getName());
  }

  protected void setObjectIdentification(DeclarationReference identification)
  {
    declarationReference = identification;
  }

  /**
   *  Sets the declaration reference.
   *
   *  @param reference
   *             declaration reference; may be null
   */
  public void setDeclarationReference(DeclarationReference reference)
  {
    declarationReference = reference;
  }

  /**
   *  Sets the declaration reference.
   *
   *  @param reference
   *             declaration reference, may be null, in such a case
   *             the declaration reference will be empty.
   */
  public final void setDeclarationReference(String reference)
  {
    if (reference != null)
      setDeclarationReference(DeclarationReference.get(reference));
    else
      setDeclarationReference((DeclarationReference)null);
  }

  /**
   *  Returns the assigned declaration reference. It will never return null.
   *
   *  @return declaration reference or null
   */
  public DeclarationReference getDeclarationReference()
  {
    return declarationReference;
  }

  /**
   *  Returns the text of the declaration reference. If it was not
   *  assigned, it returns an empty string.
   *
   *  @return the text representation of the declaration reference
   */
  public String getDeclarationReferenceText()
  {
    return declarationReference.toString();
  }


}
