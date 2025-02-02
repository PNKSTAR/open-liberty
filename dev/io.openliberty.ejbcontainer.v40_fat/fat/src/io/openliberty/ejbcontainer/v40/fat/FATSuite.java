/*******************************************************************************
 * Copyright (c) 2021 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package io.openliberty.ejbcontainer.v40.fat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import io.openliberty.ejbcontainer.v40.fat.tests.AutoCreatedNPTimerTest;

@RunWith(Suite.class)
@SuiteClasses({
                AutoCreatedNPTimerTest.class
})
public class FATSuite {
}
