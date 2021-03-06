/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.paths;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBeCanonicalPath.shouldBeCanonicalPath;
import static org.assertj.core.test.TestFailures.wasExpectingAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;

import org.assertj.core.api.exception.PathsException;
import org.junit.Test;

public class Paths_assertIsCanonical_Test extends MockPathsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
	assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> paths.assertIsCanonical(info, null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_throw_PathsException_on_io_error() throws IOException {
	final IOException exception = new IOException();
	when(actual.toRealPath()).thenThrow(exception);

    assertThatExceptionOfType(PathsException.class).isThrownBy(() -> paths.assertIsCanonical(info, actual))
                                                   .withMessage("failed to resolve actual real path")
                                                   .withCause(exception);
  }

  @Test
  public void should_fail_if_actual_real_path_differs_from_actual() throws IOException {
	final Path other = mock(Path.class);
	when(actual.toRealPath()).thenReturn(other);

	try {
	  paths.assertIsCanonical(info, actual);
	  wasExpectingAssertionError();
	} catch (AssertionError e) {
	  verify(failures).failure(info, shouldBeCanonicalPath(actual));
	}
  }

  @Test
  public void should_succeed_if_actual_real_path_is_same_as_actual() throws IOException {
	when(actual.toRealPath()).thenReturn(actual);
	paths.assertIsCanonical(info, actual);
  }
}
