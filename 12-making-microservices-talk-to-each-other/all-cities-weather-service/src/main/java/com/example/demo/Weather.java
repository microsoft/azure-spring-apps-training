/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.example.demo;

import lombok.Data;

@Data
public class Weather {
    private String city;

    private String description;

    private String icon;

}