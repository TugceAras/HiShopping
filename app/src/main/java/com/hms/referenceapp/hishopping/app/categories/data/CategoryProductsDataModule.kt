// See the License for the specific language governing permissions and
// limitations under the License.
//
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.hms.referenceapp.hishopping.app.categories.data

import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.app.categories.domain.CategoryProductsRepository
import com.hms.referenceapp.hishopping.app.categories.domain.CategoryProductsRequest
import com.hms.referenceapp.hishopping.data.model.Product
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class CategoryProductsDataModule {

    @get:Binds
    abstract val CategoryProductsDataSource.dataSource:
            BaseResultFlowAsyncDataSource<List<Product>, CategoryProductsRequest>

    @get:Binds
    abstract val CategoryProductsRepositoryImpl.repo:
            CategoryProductsRepository

}