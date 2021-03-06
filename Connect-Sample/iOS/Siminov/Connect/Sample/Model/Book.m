///
/// [SIMINOV FRAMEWORK]
/// Copyright [2014-2016] [Siminov Software Solution LLP|support@siminov.com]
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///



#import "Book.h"

@implementation Book

-(NSString *)getTitle {
    return title;
}

-(void)setTitle:(NSString *)tit {
    title = tit;
}

-(NSString *)getDescription {
    return description;
}

-(void)setDescription: (NSString *)desc {
    description = desc;
}

-(NSString *)getAuthor {
    return author;
}

-(void)setAuthor: (NSString *)aut {
    author = aut;
}

-(NSString *)getLink {
    return link;
}

-(void)setLink: (NSString *)lin {
    link = lin;
}

-(NSEnumerator *)getLessions {
    return [lessions objectEnumerator];
}

-(void)setLessions: (NSEnumerator *)less {
    
    NSString *lession;
    while (lession = [less nextObject]) {
        [lessions addObject:less];
    }
}

@end
