package ru.pasvitas.eshop.model;

public record UpdateEvent<T>(T item, UpdateType updateType) { }