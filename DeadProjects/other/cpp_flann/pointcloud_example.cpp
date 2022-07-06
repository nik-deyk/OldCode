#include <cstdlib>
#include <ctime>
#include <iostream>
#include "nanoflann.hpp"

#include "pointcloud.h"


void kdtree_demo()
{
    using std::cout;
    using std::cin;
    using std::endl;

    PointCloud<float> cloud;

    cout << ">>> Enter your food point cloud. Pass '0 0 0 0' to exit input." << endl;
    float x, y, z, f;
    char last_added = 1;
    while (last_added) {
        cout << "Please, enter next food point (calories, x, y, z): " << endl;
        cin >> f >> x >> y >> z;
        if (f != 0.0 || x != 0.0 || y != 0.0 || z != 0.0)
            cloud.pts.emplace_back(x, y, z, f);
        else
            last_added = 0;
    }

    cout << endl << ">>> Please, enter the point, where the ant is (x, y, z): " << endl;
    cin >> x >> y >> z;

    using my_kd_tree_t = nanoflann::KDTreeSingleIndexAdaptor<
        nanoflann::L2_Simple_Adaptor<float, PointCloud<float>>,
        PointCloud<float>, 3 /* dim */
        >;

    my_kd_tree_t index(3 /*dim*/, cloud, {10 /* max leaf */});

    const float query_pt[3] = {x, y, z};

    // ----------------------------------------------------------------
    // knnSearch():  Perform a search for the N closest points
    // ----------------------------------------------------------------
    {
        size_t                num_results = 1;
        std::vector<uint32_t> ret_index(num_results);
        std::vector<float>    out_dist_sqr(num_results);

        num_results = index.knnSearch(
            &query_pt[0], num_results, &ret_index[0], &out_dist_sqr[0]);

        cout << "The nearest food point: " << endl;
        uint32_t found = ret_index[0];
        cout << cloud.pts[found].representation() << endl;
    }
}

int main()
{
    kdtree_demo();
    return 0;
}
