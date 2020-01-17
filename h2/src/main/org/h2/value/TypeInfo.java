/*
 * Copyright 2004-2020 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (https://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.value;

import java.util.Objects;
import org.h2.api.ErrorCode;
import org.h2.api.IntervalQualifier;
import org.h2.message.DbException;
import org.h2.util.MathUtils;

/**
 * Data type with parameters.
 */
public class TypeInfo {

    /**
     * UNKNOWN type with parameters.
     */
    public static final TypeInfo TYPE_UNKNOWN;

    /**
     * NULL type with parameters.
     */
    public static final TypeInfo TYPE_NULL;

    /**
     * BOOLEAN type with parameters.
     */
    public static final TypeInfo TYPE_BOOLEAN;

    /**
     * TINYINT type with parameters.
     */
    public static final TypeInfo TYPE_TINYINT;

    /**
     * SMALLINT type with parameters.
     */
    public static final TypeInfo TYPE_SMALLINT;

    /**
     * INTEGER type with parameters.
     */
    public static final TypeInfo TYPE_INT;

    /**
     * BIGINT type with parameters.
     */
    public static final TypeInfo TYPE_BIGINT;

    /**
     * NUMERIC type with maximum parameters.
     */
    public static final TypeInfo TYPE_NUMERIC;

    /**
     * NUMERIC type with parameters enough to hold a BIGINT value.
     */
    public static final TypeInfo TYPE_NUMERIC_BIGINT;

    /**
     * NUMERIC type that can hold values with floating point.
     */
    public static final TypeInfo TYPE_NUMERIC_FLOATING_POINT;

    /**
     * DOUBLE PRECISION type with parameters.
     */
    public static final TypeInfo TYPE_DOUBLE;

    /**
     * REAL type with parameters.
     */
    public static final TypeInfo TYPE_REAL;

    /**
     * TIME type with maximum parameters.
     */
    public static final TypeInfo TYPE_TIME;

    /**
     * DATE type with parameters.
     */
    public static final TypeInfo TYPE_DATE;

    /**
     * TIMESTAMP type with maximum parameters.
     */
    public static final TypeInfo TYPE_TIMESTAMP;

    /**
     * BINARY VARYING type with maximum parameters.
     */
    public static final TypeInfo TYPE_VARBINARY;

    /**
     * CHARACTER VARYING type with maximum parameters.
     */
    public static final TypeInfo TYPE_VARCHAR;

    /**
     * VARCHAR_IGNORECASE type with maximum parameters.
     */
    public static final TypeInfo TYPE_VARCHAR_IGNORECASE;

    /**
     * ARRAY type with maximum parameters.
     */
    public static final TypeInfo TYPE_ARRAY;

    /**
     * RESULT_SET type with parameters.
     */
    public static final TypeInfo TYPE_RESULT_SET;

    /**
     * JAVA_OBJECT type with parameters.
     */
    public static final TypeInfo TYPE_JAVA_OBJECT;

    /**
     * UUID type with parameters.
     */
    public static final TypeInfo TYPE_UUID;

    /**
     * GEOMETRY type with default parameters.
     */
    public static final TypeInfo TYPE_GEOMETRY;

    /**
     * TIMESTAMP WITH TIME ZONE type with maximum parameters.
     */
    public static final TypeInfo TYPE_TIMESTAMP_TZ;

    /**
     * ENUM type with undefined parameters.
     */
    public static final TypeInfo TYPE_ENUM_UNDEFINED;

    /**
     * INTERVAL DAY type with maximum parameters.
     */
    public static final TypeInfo TYPE_INTERVAL_DAY;

    /**
     * INTERVAL DAY TO SECOND type with maximum parameters.
     */
    public static final TypeInfo TYPE_INTERVAL_DAY_TO_SECOND;

    /**
     * INTERVAL HOUR TO SECOND type with maximum parameters.
     */
    public static final TypeInfo TYPE_INTERVAL_HOUR_TO_SECOND;

    /**
     * ROW (row value) type with parameters.
     */
    public static final TypeInfo TYPE_ROW;

    /**
     * JSON type.
     */
    public static final TypeInfo TYPE_JSON;

    /**
     * TIME WITH TIME ZONE type with maximum parameters.
     */
    public static final TypeInfo TYPE_TIME_TZ;

    private static final TypeInfo[] TYPE_INFOS_BY_VALUE_TYPE;

    private final int valueType;

    private final long precision;

    private final int scale;

    private final int displaySize;

    private final ExtTypeInfo extTypeInfo;

    static {
        TypeInfo[] infos = new TypeInfo[Value.TYPE_COUNT];
        TYPE_UNKNOWN = new TypeInfo(Value.UNKNOWN, -1L, -1, -1, null);
        infos[Value.NULL] = TYPE_NULL = new TypeInfo(Value.NULL, ValueNull.PRECISION, 0, ValueNull.DISPLAY_SIZE, null);
        infos[Value.BOOLEAN] = TYPE_BOOLEAN = new TypeInfo(Value.BOOLEAN, ValueBoolean.PRECISION, 0,
                ValueBoolean.DISPLAY_SIZE, null);
        infos[Value.TINYINT] = TYPE_TINYINT = new TypeInfo(Value.TINYINT, ValueByte.PRECISION, 0,
                ValueByte.DISPLAY_SIZE, null);
        infos[Value.SMALLINT] = TYPE_SMALLINT = new TypeInfo(Value.SMALLINT, ValueShort.PRECISION, 0,
                ValueShort.DISPLAY_SIZE, null);
        infos[Value.INT] = TYPE_INT = new TypeInfo(Value.INT, ValueInt.PRECISION, 0, ValueInt.DISPLAY_SIZE, null);
        infos[Value.BIGINT] = TYPE_BIGINT = new TypeInfo(Value.BIGINT, ValueLong.PRECISION, 0, ValueLong.DISPLAY_SIZE,
                null);
        infos[Value.NUMERIC] = TYPE_NUMERIC = new TypeInfo(Value.NUMERIC, Integer.MAX_VALUE, //
                ValueDecimal.MAXIMUM_SCALE, Integer.MAX_VALUE, null);
        TYPE_NUMERIC_BIGINT = new TypeInfo(Value.NUMERIC, ValueLong.PRECISION, 0, ValueLong.DISPLAY_SIZE, null);
        TYPE_NUMERIC_FLOATING_POINT = new TypeInfo(Value.NUMERIC, ValueDecimal.DEFAULT_PRECISION,
                ValueDecimal.DEFAULT_PRECISION / 2, ValueDecimal.DEFAULT_PRECISION + 2, null);
        infos[Value.DOUBLE] = TYPE_DOUBLE = new TypeInfo(Value.DOUBLE, ValueDouble.PRECISION, 0,
                ValueDouble.DISPLAY_SIZE, null);
        infos[Value.REAL] = TYPE_REAL = new TypeInfo(Value.REAL, ValueFloat.PRECISION, 0, ValueFloat.DISPLAY_SIZE,
                null);
        infos[Value.TIME] = TYPE_TIME = new TypeInfo(Value.TIME, ValueTime.MAXIMUM_PRECISION, ValueTime.MAXIMUM_SCALE,
                ValueTime.MAXIMUM_PRECISION, null);
        infos[Value.DATE] = TYPE_DATE = new TypeInfo(Value.DATE, ValueDate.PRECISION, 0, ValueDate.PRECISION, null);
        infos[Value.TIMESTAMP] = TYPE_TIMESTAMP = new TypeInfo(Value.TIMESTAMP, ValueTimestamp.MAXIMUM_PRECISION,
                ValueTimestamp.MAXIMUM_SCALE, ValueTimestamp.MAXIMUM_PRECISION, null);
        infos[Value.VARBINARY] = TYPE_VARBINARY = new TypeInfo(Value.VARBINARY, Integer.MAX_VALUE, 0,
                Integer.MAX_VALUE, null);
        infos[Value.VARCHAR] = TYPE_VARCHAR = new TypeInfo(Value.VARCHAR, Integer.MAX_VALUE, 0, Integer.MAX_VALUE,
                null);
        infos[Value.VARCHAR_IGNORECASE] = TYPE_VARCHAR_IGNORECASE = new TypeInfo(Value.VARCHAR_IGNORECASE,
                Integer.MAX_VALUE, 0, Integer.MAX_VALUE, null);
        infos[Value.BLOB] = new TypeInfo(Value.BLOB, Long.MAX_VALUE, 0, Integer.MAX_VALUE, null);
        infos[Value.CLOB] = new TypeInfo(Value.CLOB, Long.MAX_VALUE, 0, Integer.MAX_VALUE, null);
        infos[Value.ARRAY] = TYPE_ARRAY = new TypeInfo(Value.ARRAY, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, null);
        infos[Value.RESULT_SET] = TYPE_RESULT_SET = new TypeInfo(Value.RESULT_SET, Integer.MAX_VALUE,
                Integer.MAX_VALUE, Integer.MAX_VALUE, null);
        infos[Value.JAVA_OBJECT] = TYPE_JAVA_OBJECT = new TypeInfo(Value.JAVA_OBJECT, Integer.MAX_VALUE, 0,
                Integer.MAX_VALUE, null);
        infos[Value.UUID] = TYPE_UUID = new TypeInfo(Value.UUID, ValueUuid.PRECISION, 0, ValueUuid.DISPLAY_SIZE, null);
        infos[Value.CHAR] = new TypeInfo(Value.CHAR, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, null);
        infos[Value.GEOMETRY] = TYPE_GEOMETRY = new TypeInfo(Value.GEOMETRY, Integer.MAX_VALUE, 0, Integer.MAX_VALUE,
                null);
        infos[Value.TIMESTAMP_TZ] = TYPE_TIMESTAMP_TZ = new TypeInfo(Value.TIMESTAMP_TZ,
                ValueTimestampTimeZone.MAXIMUM_PRECISION, ValueTimestamp.MAXIMUM_SCALE,
                ValueTimestampTimeZone.MAXIMUM_PRECISION, null);
        infos[Value.ENUM] = TYPE_ENUM_UNDEFINED = new TypeInfo(Value.ENUM, Integer.MAX_VALUE, 0, Integer.MAX_VALUE,
                null);
        for (int i = Value.INTERVAL_YEAR; i <= Value.INTERVAL_MINUTE_TO_SECOND; i++) {
            infos[i] = new TypeInfo(i, ValueInterval.MAXIMUM_PRECISION,
                    IntervalQualifier.valueOf(i - Value.INTERVAL_YEAR).hasSeconds() ? ValueInterval.MAXIMUM_SCALE : 0,
                    ValueInterval.getDisplaySize(i, ValueInterval.MAXIMUM_PRECISION,
                            // Scale will be ignored if it is not supported
                            ValueInterval.MAXIMUM_SCALE),
                    null);
        }
        TYPE_INTERVAL_DAY = infos[Value.INTERVAL_DAY];
        TYPE_INTERVAL_DAY_TO_SECOND = infos[Value.INTERVAL_DAY_TO_SECOND];
        TYPE_INTERVAL_HOUR_TO_SECOND = infos[Value.INTERVAL_HOUR_TO_SECOND];
        infos[Value.ROW] = TYPE_ROW = new TypeInfo(Value.ROW, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, null);
        infos[Value.JSON] = TYPE_JSON = new TypeInfo(Value.JSON, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, null);
        infos[Value.TIME_TZ] = TYPE_TIME_TZ = new TypeInfo(Value.TIME_TZ, ValueTimeTimeZone.MAXIMUM_PRECISION,
                ValueTime.MAXIMUM_SCALE, ValueTimeTimeZone.MAXIMUM_PRECISION, null);
        TYPE_INFOS_BY_VALUE_TYPE = infos;
    }

    /**
     * Get the data type with parameters object for the given value type and
     * maximum parameters.
     *
     * @param type
     *            the value type
     * @return the data type with parameters object
     */
    public static TypeInfo getTypeInfo(int type) {
        if (type == Value.UNKNOWN) {
            throw DbException.get(ErrorCode.UNKNOWN_DATA_TYPE_1, "?");
        }
        if (type >= Value.NULL && type < Value.TYPE_COUNT) {
            TypeInfo t = TYPE_INFOS_BY_VALUE_TYPE[type];
            if (t != null) {
                return t;
            }
        }
        return TYPE_NULL;
    }

    /**
     * Get the data type with parameters object for the given value type and the
     * specified parameters.
     *
     * @param type
     *            the value type
     * @param precision
     *            the precision
     * @param scale
     *            the scale
     * @param extTypeInfo
     *            the extended type information, or null
     * @return the data type with parameters object
     */
    public static TypeInfo getTypeInfo(int type, long precision, int scale, ExtTypeInfo extTypeInfo) {
        switch (type) {
        case Value.NULL:
        case Value.BOOLEAN:
        case Value.TINYINT:
        case Value.SMALLINT:
        case Value.INT:
        case Value.BIGINT:
        case Value.DOUBLE:
        case Value.REAL:
        case Value.DATE:
        case Value.RESULT_SET:
        case Value.JAVA_OBJECT:
        case Value.UUID:
        case Value.ROW:
        case Value.JSON:
            return TYPE_INFOS_BY_VALUE_TYPE[type];
        case Value.UNKNOWN:
            return TYPE_UNKNOWN;
        case Value.NUMERIC:
            if (precision < 0) {
                precision = ValueDecimal.DEFAULT_PRECISION;
            } else if (precision > Integer.MAX_VALUE) {
                precision = Integer.MAX_VALUE;
            }
            return new TypeInfo(Value.NUMERIC, precision, scale, MathUtils.convertLongToInt(precision + 2), null);
        case Value.TIME: {
            if (scale < 0 || scale >= ValueTime.MAXIMUM_SCALE) {
                return TYPE_TIME;
            }
            int d = scale == 0 ? 8 : 9 + scale;
            return new TypeInfo(Value.TIME, d, scale, d, null);
        }
        case Value.TIME_TZ: {
            if (scale < 0 || scale >= ValueTime.MAXIMUM_SCALE) {
                return TYPE_TIME_TZ;
            }
            int d = scale == 0 ? 14 : 15 + scale;
            return new TypeInfo(Value.TIME_TZ, d, scale, d, null);
        }
        case Value.TIMESTAMP: {
            if (scale < 0 || scale >= ValueTimestamp.MAXIMUM_SCALE) {
                return TYPE_TIMESTAMP;
            }
            int d = scale == 0 ? 19 : 20 + scale;
            return new TypeInfo(Value.TIMESTAMP, d, scale, d, null);
        }
        case Value.TIMESTAMP_TZ: {
            if (scale < 0 || scale >= ValueTimestamp.MAXIMUM_SCALE) {
                return TYPE_TIMESTAMP_TZ;
            }
            int d = scale == 0 ? 25 : 26 + scale;
            return new TypeInfo(Value.TIMESTAMP_TZ, d, scale, d, null);
        }
        case Value.VARBINARY:
            if (precision < 0 || precision > Integer.MAX_VALUE) {
                return TYPE_VARBINARY;
            }
            return new TypeInfo(Value.VARBINARY, precision, 0, MathUtils.convertLongToInt(precision * 2), null);
        case Value.VARCHAR:
            if (precision < 0 || precision >= Integer.MAX_VALUE) {
                return TYPE_VARCHAR;
            }
            return new TypeInfo(Value.VARCHAR, precision, 0, (int) precision, null);
        case Value.VARCHAR_IGNORECASE:
            if (precision < 0 || precision >= Integer.MAX_VALUE) {
                return TYPE_VARCHAR_IGNORECASE;
            }
            return new TypeInfo(Value.VARCHAR_IGNORECASE, precision, 0, (int) precision, null);
        case Value.BLOB:
        case Value.CLOB:
            if (precision < 0) {
                precision = Long.MAX_VALUE;
            }
            return new TypeInfo(type, precision, 0, MathUtils.convertLongToInt(precision), null);
        case Value.ARRAY:
            if (precision < 0 || precision >= Integer.MAX_VALUE) {
                if (extTypeInfo == null) {
                    return TYPE_ARRAY;
                }
                precision = Integer.MAX_VALUE;
            }
            return new TypeInfo(Value.ARRAY, precision, 0, Integer.MAX_VALUE, extTypeInfo);
        case Value.CHAR:
            if (precision < 0 || precision > Integer.MAX_VALUE) {
                precision = Integer.MAX_VALUE;
            }
            return new TypeInfo(Value.CHAR, precision, 0, (int) precision, null);
        case Value.GEOMETRY:
            if (extTypeInfo instanceof ExtTypeInfoGeometry) {
                return new TypeInfo(Value.GEOMETRY, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, extTypeInfo);
            } else {
                return TYPE_GEOMETRY;
            }
        case Value.ENUM:
            if (extTypeInfo instanceof ExtTypeInfoEnum) {
                return ((ExtTypeInfoEnum) extTypeInfo).getType();
            } else {
                return TYPE_ENUM_UNDEFINED;
            }
        case Value.INTERVAL_YEAR:
        case Value.INTERVAL_MONTH:
        case Value.INTERVAL_DAY:
        case Value.INTERVAL_HOUR:
        case Value.INTERVAL_MINUTE:
        case Value.INTERVAL_YEAR_TO_MONTH:
        case Value.INTERVAL_DAY_TO_HOUR:
        case Value.INTERVAL_DAY_TO_MINUTE:
        case Value.INTERVAL_HOUR_TO_MINUTE:
            if (precision < 1 || precision > ValueInterval.MAXIMUM_PRECISION) {
                precision = ValueInterval.MAXIMUM_PRECISION;
            }
            return new TypeInfo(type, precision, 0, ValueInterval.getDisplaySize(type, (int) precision, 0), null);
        case Value.INTERVAL_SECOND:
        case Value.INTERVAL_DAY_TO_SECOND:
        case Value.INTERVAL_HOUR_TO_SECOND:
        case Value.INTERVAL_MINUTE_TO_SECOND:
            if (precision < 1 || precision > ValueInterval.MAXIMUM_PRECISION) {
                precision = ValueInterval.MAXIMUM_PRECISION;
            }
            if (scale < 0 || scale > ValueInterval.MAXIMUM_SCALE) {
                scale = ValueInterval.MAXIMUM_SCALE;
            }
            return new TypeInfo(type, precision, scale, ValueInterval.getDisplaySize(type, (int) precision, scale),
                    null);
        }
        return TYPE_NULL;
    }

    /**
     * Creates new instance of data type with parameters.
     *
     * @param valueType
     *            the value type
     * @param precision
     *            the precision
     * @param scale
     *            the scale
     * @param displaySize
     *            the display size in characters
     * @param extTypeInfo
     *            the extended type information, or null
     */
    public TypeInfo(int valueType, long precision, int scale, int displaySize, ExtTypeInfo extTypeInfo) {
        this.valueType = valueType;
        this.precision = precision;
        this.scale = scale;
        this.displaySize = displaySize;
        this.extTypeInfo = extTypeInfo;
    }

    /**
     * Returns the value type.
     *
     * @return the value type
     */
    public int getValueType() {
        return valueType;
    }

    /**
     * Returns the precision.
     *
     * @return the precision
     */
    public long getPrecision() {
        return precision;
    }

    /**
     * Returns the scale.
     *
     * @return the scale
     */
    public int getScale() {
        return scale;
    }

    /**
     * Returns the display size in characters.
     *
     * @return the display size
     */
    public int getDisplaySize() {
        return displaySize;
    }

    /**
     * Returns the extended type information, or null.
     *
     * @return the extended type information, or null
     */
    public ExtTypeInfo getExtTypeInfo() {
        return extTypeInfo;
    }

    /**
     * Appends SQL representation of this object to the specified string
     * builder.
     *
     * @param builder
     *            string builder
     * @return the specified string builder
     */
    public StringBuilder getSQL(StringBuilder builder) {
        switch (valueType) {
        case Value.NUMERIC:
            // Can be DECIMAL or NUMERIC
            builder.append(DataType.getDataType(valueType).name);
            builder.append('(').append(precision).append(", ").append(scale).append(')');
            break;
        case Value.VARBINARY:
        case Value.VARCHAR:
        case Value.VARCHAR_IGNORECASE:
        case Value.CHAR:
            builder.append(DataType.getDataType(valueType).name);
            if (precision < Integer.MAX_VALUE) {
                builder.append('(').append(precision).append(')');
            }
            break;
        case Value.TIME:
        case Value.TIME_TZ:
            builder.append("TIME");
            if (scale != ValueTime.DEFAULT_SCALE) {
                builder.append('(').append(scale).append(')');
            }
            if (valueType == Value.TIME_TZ) {
                builder.append(" WITH TIME ZONE");
            }
            break;
        case Value.TIMESTAMP:
        case Value.TIMESTAMP_TZ:
            builder.append("TIMESTAMP");
            if (scale != ValueTimestamp.DEFAULT_SCALE) {
                builder.append('(').append(scale).append(')');
            }
            if (valueType == Value.TIMESTAMP_TZ) {
                builder.append(" WITH TIME ZONE");
            }
            break;
        case Value.INTERVAL_YEAR:
        case Value.INTERVAL_MONTH:
        case Value.INTERVAL_DAY:
        case Value.INTERVAL_HOUR:
        case Value.INTERVAL_MINUTE:
        case Value.INTERVAL_SECOND:
        case Value.INTERVAL_YEAR_TO_MONTH:
        case Value.INTERVAL_DAY_TO_HOUR:
        case Value.INTERVAL_DAY_TO_MINUTE:
        case Value.INTERVAL_DAY_TO_SECOND:
        case Value.INTERVAL_HOUR_TO_MINUTE:
        case Value.INTERVAL_HOUR_TO_SECOND:
        case Value.INTERVAL_MINUTE_TO_SECOND:
            IntervalQualifier.valueOf(valueType - Value.INTERVAL_YEAR).getTypeName(builder,
                    precision == ValueInterval.DEFAULT_PRECISION ? -1 : (int) precision,
                    scale == ValueInterval.DEFAULT_SCALE ? -1 : scale, false);
            break;
        case Value.ARRAY:
            if (extTypeInfo != null) {
                builder.append(extTypeInfo.getCreateSQL()).append(' ');
            }
            builder.append("ARRAY");
            if (precision < Integer.MAX_VALUE) {
                builder.append('[').append(precision).append(']');
            }
            break;
        case Value.ENUM:
            builder.append("ENUM").append(extTypeInfo.getCreateSQL());
            break;
        case Value.GEOMETRY:
            builder.append("GEOMETRY");
            if (extTypeInfo != null) {
                builder.append(extTypeInfo.getCreateSQL());
            }
            break;
        default:
            builder.append(DataType.getDataType(valueType).name);
        }
        return builder;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + valueType;
        result = 31 * result + (int) (precision ^ (precision >>> 32));
        result = 31 * result + scale;
        result = 31 * result + displaySize;
        result = 31 * result + ((extTypeInfo == null) ? 0 : extTypeInfo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != TypeInfo.class) {
            return false;
        }
        TypeInfo other = (TypeInfo) obj;
        return valueType == other.valueType && precision == other.precision && scale == other.scale
                && displaySize == other.displaySize && Objects.equals(extTypeInfo, other.extTypeInfo);
    }

    @Override
    public String toString() {
        return getSQL(new StringBuilder()).toString();
    }

    /**
     * Convert this type information to compatible NUMERIC type information.
     *
     * @return NUMERIC type information
     */
    public TypeInfo toNumericType() {
        switch (valueType) {
        case Value.BOOLEAN:
        case Value.TINYINT:
        case Value.SMALLINT:
        case Value.INT:
            return getTypeInfo(Value.NUMERIC, precision, 0, null);
        case Value.BIGINT:
            return TYPE_NUMERIC_BIGINT;
        case Value.NUMERIC:
            return this;
        case Value.REAL:
            // Smallest REAL value is 1.4E-45 with precision 2 and scale 46
            // Largest REAL value is 3.4028235E+38 with precision 8 and scale -31
            return getTypeInfo(Value.NUMERIC, 85, 46, null);
        case Value.DOUBLE:
            // Smallest DOUBLE value is 4.9E-324 with precision 2 and scale 325
            // Largest DOUBLE value is 1.7976931348623157E+308 with precision 17
            // and scale -292
            return getTypeInfo(Value.NUMERIC, 634, 325, null);
        default:
            return TYPE_NUMERIC_FLOATING_POINT;
        }
    }

}
