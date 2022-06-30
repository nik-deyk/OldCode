#include <cstdlib>
#include <iostream>
#include <vector>
#include <memory>
#include <string>
#include <stdexcept>

template<typename ... Args>
std::string string_format( const std::string& format, Args ... args )
{
    int size_s = std::snprintf( nullptr, 0, format.c_str(), args ... ) + 1; // Extra space for '\0'
    if( size_s <= 0 ){ throw std::runtime_error( "Error during formatting." ); }
    auto size = static_cast<size_t>( size_s );
    std::unique_ptr<char[]> buf( new char[ size ] );
    std::snprintf( buf.get(), size, format.c_str(), args ... );
    return std::string( buf.get(), buf.get() + size - 1 ); // We don't want the '\0' inside
}

template <typename T>
struct PointCloud
{
    struct Point
    {
        T x, y, z;
        float calories;

        Point(const T& x, const T& y, const T& z, float calories) : x(x), y(y), z(z), calories(calories) {}

        std::string representation() const { 
            return string_format("X=%.1f, Y=%.1f, Z=%.1f\tCal\t%.1f", x, y, z, calories);
        }
    };

    using coord_t = T;

    std::vector<Point> pts;

    inline size_t kdtree_get_point_count() const { return pts.size(); }

    inline T kdtree_get_pt(const size_t idx, const size_t dim) const
    {
        if (dim == 0)
            return pts[idx].x;
        else if (dim == 1)
            return pts[idx].y;
        else
            return pts[idx].z;
    }

    template <class BBOX>
    bool kdtree_get_bbox(BBOX& /* bb */) const
    {
        return false;
    }
};
