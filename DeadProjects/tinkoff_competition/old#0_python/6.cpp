#include <vector>
#include <iostream>
#include <map>

const long long int MOD = 1000000007;

class Vertex;

std::vector<Vertex*> vertexes;
int n, k;

class Vertex {
public:
	int number = 0;

	std::vector<long long int> groups;

	Vertex(int a, int i) : number(a) {
		groups = std::vector<long long int>(n - i, 0);
		groups[0] = 1;
	}
};

void fill_new_group(std::vector<long long int>& groups_to_fill, int j) {
	bool check_is_smaller = j != 0;
	int check_value = 0;
	if (check_is_smaller) {
		check_value = vertexes[j - 1]->number;
	}
	for (int i = j; i < n; i++) {
		if (!check_is_smaller || check_value > vertexes[i]->number) {
			auto& they_gr = vertexes[i]->groups;
			for (unsigned int p = 0; p < they_gr.size(); p++) {
				if (p + 1 <= k) {
					groups_to_fill[p + 1] = (groups_to_fill[p + 1] + they_gr[p]) % MOD;
				}
			}
		}
	}
}

int main() {
	std::cin >> n >> k;

	if (k > n) {
		std::cout << 0 << std::endl;
		return 0;
	}

	vertexes = std::vector<Vertex*>(n);
	for (unsigned int i = 0; i < vertexes.size(); i++) {
		int a;
		std::cin >> a;
		vertexes[i] = new Vertex(a, i);
	}
	
	for (int i = static_cast<int>(vertexes.size()) - 2; i >= 0; i--) {
		fill_new_group(vertexes[i]->groups, i+1);
	}
	auto init_group = std::vector<long long int>(n+1, 0);

	fill_new_group(init_group, 0);

	std::cout << init_group[k] << ' ';

	return 0;
}